package com.aliyun.kms.kms20160120.benchmarks;

import com.aliyun.dkms.gcs.sdk.models.EncryptResponse;
import com.aliyun.dkms.gcs.sdk.models.SignResponse;
import com.aliyun.kms.kms20160120.Client;
import com.aliyun.kms.kms20160120.benchmarks.workers.*;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class BenchmarkBuilder {
    private final Config config;
    private final CountCollect countCollect;
    private final Logger reportLogger;
    private final Logger debugLogger;
    private Worker worker = null;

    public BenchmarkBuilder(Config config, Logger reportLogger, Logger debugLogger) throws Exception {
        this.config = config;
        this.reportLogger = reportLogger;
        this.debugLogger = debugLogger;
        this.countCollect = new CountCollect(config.getConcurrenceNums());
        this.initWorker();
    }

    private void initWorker() throws Exception {
        com.aliyun.dkms.gcs.openapi.models.Config config = new com.aliyun.dkms.gcs.openapi.models.Config();
        config.clientKeyFile = this.config.getClientKeyPath();
        config.password = this.config.getClientKeyPassword();
        config.endpoint = this.config.getEndpoint();
        config.caFilePath = this.config.getCaFilePath();
        config.maxIdleConns = this.config.getConcurrenceNums();

        Client client = new Client(config);

        switch (this.config.getCaseName()) {
            case "encrypt":
                this.worker = new EncryptWorker(client, this.config.getKeyId(), this.config.getPlainText().getBytes(StandardCharsets.UTF_8), this.config.getAad());
                break;
            case "decrypt":
                EncryptWorker encryptWorker = new EncryptWorker(client, this.config.getKeyId(), this.config.getPlainText().getBytes(StandardCharsets.UTF_8), this.config.getAad());
                EncryptResponse encRes = encryptWorker.encrypt();
                this.worker = new DecryptWorker(client, this.config.getKeyId(), encRes.getCiphertextBlob(), encRes.getIv(), this.config.getAad());
                break;
            case "sign":
                this.worker = new SignWorker(client, this.config.getKeyId(), this.config.getDigest(), "DIGEST");
                break;
            case "verify":
                SignWorker signWorker = new SignWorker(client, this.config.getKeyId(), this.config.getDigest(), "DIGEST");
                SignResponse signRes = signWorker.sign();
                this.worker = new VerifyWorker(client, this.config.getKeyId(), this.config.getDigest(), "DIGEST", signRes.getSignature());
                break;
            case "get_secret_value":
                this.worker = new GetSecretValueWorker(client, this.config.getSecretName());
                break;
            default:
                throw new Exception(String.format("invalid benchmark case name:%s", this.config.getCaseName()));
        }
    }

    public void run() {
        CountDownLatch waitThreadsStart = new CountDownLatch(this.config.getConcurrenceNums());
        CountDownLatch waitThreadsEnd = new CountDownLatch(this.config.getConcurrenceNums());
        AtomicBoolean finishChan = new AtomicBoolean(false);

        // 启动压测
        for (int i = 0; i < this.config.getConcurrenceNums(); i++) {
            this.countCollect.requestCountList.add(new AtomicLong(0));
            this.countCollect.responseCountList.add(new AtomicLong(0));
            this.countCollect.timeCostSumPerThreadList.add(new AtomicLong(0));
            this.countCollect.countPerThreadList.add(new AtomicLong(0));
            this.countCollect.minOnceTimeCostList.add(new AtomicLong(0));
            this.countCollect.maxOnceTimeCostList.add(new AtomicLong(0));
            this.countCollect.periodTpsList.add(new AtomicLong(0));
            new Thread(new ExecuteWorkerTask(this.countCollect, waitThreadsStart, waitThreadsEnd, finishChan, this.config.getDuration() * 1000L, i)).start();
            waitThreadsStart.countDown();
        }

        long beginTime = System.currentTimeMillis();
        this.countCollect.analysisLastTime.set(beginTime);

        // 定时输出统计日志
        Timer timer = new Timer();
        AtomicLong count = new AtomicLong(0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                count.incrementAndGet();
                reportLogger.info(String.format("----------------- Time_%d: [%s]--------------", count.get(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                countCollect.runBenchTimeCost.set(System.currentTimeMillis() - beginTime);
                long period = System.currentTimeMillis() - countCollect.analysisLastTime.get();
                countCollect.analysisLastTime.set(System.currentTimeMillis());
                countCollect.Analysis(reportLogger, period);
            }
        }, 1000L, this.config.getPeriod()*1000L);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            reportLogger.info(String.format("----------------- Statistics: [%s]--------------", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
            countCollect.runBenchTimeCost.set(System.currentTimeMillis() - beginTime);
            long period = System.currentTimeMillis() - countCollect.analysisLastTime.get();
            countCollect.analysisLastTime.set(System.currentTimeMillis());
            countCollect.Analysis(reportLogger, period);
        }));

        // 等待压测完成
        try {
            waitThreadsEnd.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        // 关闭定时器
        timer.cancel();
    }

    public class ExecuteWorkerTask implements Runnable {
        private final CountCollect countCollect;
        private final CountDownLatch waitThreadsStart;
        private final CountDownLatch waitThreadsEnd;
        private final AtomicBoolean finished;
        private final long durationMillis;
        private final int index;

        ExecuteWorkerTask(CountCollect countCollect, CountDownLatch waitThreadsStart, CountDownLatch waitThreadsEnd, AtomicBoolean finished, long durationMillis, int index) {
            this.countCollect = countCollect;
            this.waitThreadsStart = waitThreadsStart;
            this.waitThreadsEnd = waitThreadsEnd;
            this.finished = finished;
            this.durationMillis = durationMillis;
            this.index = index;
        }

        @Override
        public void run() {
            if (waitThreadsStart != null) {
                try {
                    waitThreadsStart.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
            long start = System.currentTimeMillis();
            long count = 0L;
            while (!finished.get()) {
                this.countCollect.requestCountList.get(index).incrementAndGet();
                long onceTimeStart = System.currentTimeMillis();
                String requestId = "";
                Exception err = null;
                try {
                    requestId = worker.doAction();
                } catch (Exception e) {
                    err = e;
                } finally {
                    long onceTimeCost = System.currentTimeMillis() - onceTimeStart;
                    // 更新统计数据
                    countCollect.updateCountAndCost(index, onceTimeCost, err);
                    if (debugLogger != null) {
                        String debugMsg = String.format("[BenchDebug_%d]\tonceTimeCost: %dms\tRequestId: %s", count, onceTimeCost, requestId);
                        if (err != null) {
                            debugMsg = String.format("[BenchError_%d]\tError: %s", count, err.getMessage());
                        }
                        debugLogger.info(debugMsg);
                    }
                }
                if (durationMillis != -1 && System.currentTimeMillis() - start > durationMillis) {
                    break;
                }
                count++;
            }
            waitThreadsEnd.countDown();
        }
    }

    public class CountCollect {
        private final List<AtomicLong> requestCountList;
        private final List<AtomicLong> responseCountList;
        private final AtomicLong responseLimitCount;
        private final AtomicLong timeoutErrorCount;
        private final AtomicLong clientErrorCount;
        private final List<AtomicLong> timeCostSumPerThreadList;
        private final List<AtomicLong> countPerThreadList;
        private final AtomicLong runBenchTimeCost;
        private final List<AtomicLong> minOnceTimeCostList;
        private final List<AtomicLong> maxOnceTimeCostList;
        private final List<AtomicLong> periodTpsList;
        private final AtomicLong analysisLastTime;

        public CountCollect(Integer concurrenceNums) {
            this.requestCountList = new ArrayList<>(concurrenceNums);
            this.responseCountList = new ArrayList<>(concurrenceNums);
            this.responseLimitCount = new AtomicLong(0L);
            this.timeoutErrorCount = new AtomicLong(0);
            this.clientErrorCount = new AtomicLong(0);
            this.timeCostSumPerThreadList = new ArrayList<>(concurrenceNums);
            this.countPerThreadList = new ArrayList<>(concurrenceNums);
            this.runBenchTimeCost = new AtomicLong(0L);
            this.minOnceTimeCostList = new ArrayList<>(concurrenceNums);
            this.maxOnceTimeCostList = new ArrayList<>(concurrenceNums);
            this.periodTpsList = new ArrayList<>(concurrenceNums);
            this.analysisLastTime = new AtomicLong(0L);
        }

        public void Analysis(Logger logger, Long period) {
            long allThreadCount = 0L;
            long allThreadTimeCost = 0L;
            long allRequestCount = 0L;
            long allResponseCount = 0L;
            long allPeriodTps = 0L;

            for (int i = 0; i < this.requestCountList.size(); i++) {
                allThreadCount += this.countPerThreadList.get(i).get();
                allThreadTimeCost += this.timeCostSumPerThreadList.get(i).get();
                allRequestCount += this.requestCountList.get(i).get();
                allResponseCount += this.responseCountList.get(i).get();
                allPeriodTps += this.periodTpsList.get(i).getAndSet(0);
            }

            long maxDuration = 0L;
            long minDuration = Long.MAX_VALUE;

            for (AtomicLong duration : this.minOnceTimeCostList) {
                if (minDuration > duration.get()) {
                    minDuration = duration.get();
                }
            }
            for (AtomicLong duration : this.maxOnceTimeCostList) {
                if (maxDuration < duration.get()) {
                    maxDuration = duration.get();
                }
            }
            if(period == 0){
                period = 1L;
            }
            if(allThreadCount == 0){
                allThreadCount = 1L;
            }
            String record = String.format(
                    "[Benchmark-Detail]\tRequestCount: %d\tResponseCount: %d\tTPS: %d\tAvgTPS: %d\n" +
                            "MaxOnceTimeCost: %d ms\tMinOnceTimeCost: %d ms\tAvgOnceTimeCost: %d ms\n" +
                            "ClientErrorCount: %d\tLimitExceededErrorCount: %d\tTimeoutErrorCount: %d",
                    allRequestCount,
                    allResponseCount,
                    (long)(allPeriodTps / ((double)period/1000)),
                    (long)(allThreadCount / ((double)this.runBenchTimeCost.get()/1000)),
                    maxDuration, minDuration, allThreadTimeCost / allThreadCount, clientErrorCount.get(),
                    responseLimitCount.get(), timeoutErrorCount.get());

            if (logger != null) {
                logger.info(record);
            }
        }

        public void updateCountAndCost(int index, long onceTimeCost, Exception e) {
            // 单线程请求总耗时
            this.timeCostSumPerThreadList.get(index).addAndGet(onceTimeCost);
            // 单线程请求总数
            this.countPerThreadList.get(index).incrementAndGet();
            if (countCollect.maxOnceTimeCostList.get(index).get() < onceTimeCost) {
                // 单次请求最大耗时
                countCollect.maxOnceTimeCostList.get(index).getAndSet(onceTimeCost);
            }
            if (countCollect.minOnceTimeCostList.get(index).get() > onceTimeCost) {
                // 单次请求最小耗时
                countCollect.minOnceTimeCostList.get(index).getAndSet(onceTimeCost);
            }
            // 响应总次数
            this.responseCountList.get(index).incrementAndGet();
            // 每个输出周期Tps
            this.periodTpsList.get(index).incrementAndGet();
            if (e != null) {
                if (e.getMessage().toLowerCase().contains("timeout")) {
                    this.timeoutErrorCount.incrementAndGet();
                } else if (e.getMessage().contains("Limit Exceeded")) {
                    this.responseLimitCount.incrementAndGet();
                } else {
                    this.clientErrorCount.incrementAndGet();
                }
            }
        }
    }
}
