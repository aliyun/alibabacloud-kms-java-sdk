package com.aliyun.kms.kms20160120.benchmarks;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Config {
    private String caseName;
    private String endpoint;
    private String keyId;
    private String clientKeyPath;
    private String clientKeyPassword;
    private int dataSize;
    private int concurrenceNums;
    private int duration;
    private int period;
    private String logPath;
    private boolean enableDebugLog;
    private String caFilePath;
    private String secretName;
    private String algorithm;
    private String plainText;
    private byte[] aad;
    private byte[] digest;
    private String ca;

    public Config() {
    }

    public Config(String caseName, String endpoint, String keyId, String clientKeyPath, String clientKeyPassword, int dataSize,
                  int concurrenceNums, int duration, int period, String logPath, boolean enableDebugLog, String caFilePath, String secretName) {
        this.caseName = caseName;
        this.endpoint = endpoint;
        this.keyId = keyId;
        this.clientKeyPath = clientKeyPath;
        this.clientKeyPassword = clientKeyPassword;
        this.dataSize = dataSize;
        this.concurrenceNums = concurrenceNums;
        this.duration = duration;
        this.period = period;
        this.logPath = logPath;
        this.enableDebugLog = enableDebugLog;
        this.caFilePath = caFilePath;
        this.secretName = secretName;
    }

    public String getCaseName() {
        return caseName;
    }

    public Config setCaseName(String caseName) {
        this.caseName = caseName;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Config setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getKeyId() {
        return keyId;
    }

    public Config setKeyId(String keyId) {
        this.keyId = keyId;
        return this;
    }

    public String getClientKeyPath() {
        return clientKeyPath;
    }

    public Config setClientKeyPath(String clientKeyPath) {
        this.clientKeyPath = clientKeyPath;
        return this;
    }

    public String getClientKeyPassword() {
        return clientKeyPassword;
    }

    public Config setClientKeyPassword(String clientKeyPassword) {
        this.clientKeyPassword = clientKeyPassword;
        return this;
    }

    public int getDataSize() {
        return dataSize;
    }

    public Config setDataSize(int dataSize) {
        this.dataSize = dataSize;
        return this;
    }

    public int getConcurrenceNums() {
        return concurrenceNums;
    }

    public Config setConcurrenceNums(int concurrenceNums) {
        this.concurrenceNums = concurrenceNums;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Config setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public int getPeriod() {
        return period;
    }

    public Config setPeriod(int period) {
        this.period = period;
        return this;
    }

    public String getLogPath() {
        return logPath;
    }

    public Config setLogPath(String logPath) {
        this.logPath = logPath;
        return this;
    }

    public boolean isEnableDebugLog() {
        return enableDebugLog;
    }

    public Config setEnableDebugLog(boolean enableDebugLog) {
        this.enableDebugLog = enableDebugLog;
        return this;
    }

    public String getCaFilePath() {
        return caFilePath;
    }

    public Config setCaFilePath(String caFilePath) {
        this.caFilePath = caFilePath;
        return this;
    }

    public String getSecretName() {
        return secretName;
    }

    public Config setSecretName(String secretName) {
        this.secretName = secretName;
        return this;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Config setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public String getPlainText() {
        return plainText;
    }

    public Config setPlainText(String plainText) {
        this.plainText = plainText;
        return this;
    }

    public byte[] getAad() {
        return this.aad;
    }

    public Config setAad(byte[] aad) {
        this.aad = aad;
        return this;
    }

    public byte[] getDigest() {
        return digest;
    }

    public Config setDigest(byte[] digest) {
        this.digest = digest;
        return this;
    }

    public String getCa() {
        return ca;
    }

    public Config setCa(String ca) {
        this.ca = ca;
        return this;
    }

    public void applyFlag() throws IllegalArgumentException, NoSuchAlgorithmException, IOException {
        checkParams();
        if (this.plainText == null || this.plainText.isEmpty() || this.plainText.length() != this.dataSize) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < this.dataSize; i++) {
                builder.append("1");
            }
            this.plainText = builder.toString();
        }
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        this.digest = sha256.digest(this.plainText.getBytes(StandardCharsets.UTF_8));
        if (!this.caFilePath.isEmpty()) {
            this.ca = new String(Files.readAllBytes(Paths.get(this.caFilePath)));
        }
        this.aad = "this is encryption context".getBytes(StandardCharsets.UTF_8);
    }

    public void checkParams() {
        if (this.caseName.isEmpty()) {
            throw new IllegalArgumentException("'case' can not be empty");
        }
        if (this.endpoint.isEmpty()) {
            throw new IllegalArgumentException("'endpoint' can not be empty");
        }
        if (this.keyId.isEmpty()) {
            if (!this.caseName.equals("get_secret_value")) {
                throw new IllegalArgumentException("'key_id' can not be empty");
            }
        }
        if (this.clientKeyPath.isEmpty()) {
            throw new IllegalArgumentException("'client_key_path' can not be empty");
        }
        if (this.clientKeyPassword.isEmpty()) {
            throw new IllegalArgumentException("'client_key_password' can not be empty");
        }
        if (this.secretName.isEmpty()) {
            if (this.caseName.equals("get_secret_value")) {
                throw new IllegalArgumentException("'secret_name' can not be empty");
            }
        }
    }
}