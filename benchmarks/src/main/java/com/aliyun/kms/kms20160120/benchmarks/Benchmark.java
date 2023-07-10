package com.aliyun.kms.kms20160120.benchmarks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Benchmark {

    public static void main(String[] args) {
        try {
            Map<String, String> argsList = getArgs(args);
            String caseName = argsList.getOrDefault("case", "");
            String endpoint = argsList.getOrDefault("endpoint", "");
            String clientKeyPath = argsList.getOrDefault("client_key_path", "");
            String clientKeyPassword = argsList.getOrDefault("client_key_password", "");
            int concurrenceNums = Integer.parseInt(argsList.getOrDefault("concurrence_nums", "32"));
            int duration = Integer.parseInt(argsList.getOrDefault("duration", "600"));
            int period = Integer.parseInt(argsList.getOrDefault("period", "1"));
            String logPath = argsList.getOrDefault("log_path", "");
            boolean enableDebugLog = argsList.getOrDefault("enable_debug_log", "false").equals("true");
            String keyId = argsList.getOrDefault("key_id", "");
            int dataSize = Integer.parseInt(argsList.getOrDefault("data_size", "32"));
            String secretName = argsList.getOrDefault("secret_name", "");
            String caPath = argsList.getOrDefault("ca_path", "");

            Config config = new Config(caseName, endpoint, keyId, clientKeyPath, clientKeyPassword, dataSize, concurrenceNums, duration, period, logPath, enableDebugLog, caPath, secretName);
            config.applyFlag();

            Logger reportLog = null;
            Logger debugLog = null;

            if (!logPath.equals("")) {
                System.setProperty("logPath", logPath);
                reportLog = LoggerFactory.getLogger("benchmarkLog");
                if (config.isEnableDebugLog()) {
                    debugLog = LoggerFactory.getLogger("debugLog");
                }
            } else {
                reportLog = LoggerFactory.getLogger("stdout");
                if (config.isEnableDebugLog()) {
                    debugLog = LoggerFactory.getLogger("stdout");
                }
            }

            new BenchmarkBuilder(config, reportLog, debugLog).run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> getArgs(String[] args) {
        Map<String, String> res = new HashMap<>();
        for (String arg : args) {
            String[] params = arg.split("=", 2);
            if (params.length == 2) {
                res.put(params[0].trim().replaceAll("-", ""), params[1].trim());
            }
        }
        return res;
    }

}
