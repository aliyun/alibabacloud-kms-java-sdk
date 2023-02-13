package com.aliyun.kms.kms20160120.model;


import java.nio.charset.Charset;

public class KmsRuntimeOptions extends com.aliyun.teautil.models.RuntimeOptions {

    /**
     * 是否使用KMS共享网关
     */
    private Boolean isUseKmsShareGateway;
    /**
     * 指定使用到的字符集编码
     */
    private Charset charset;
    /**
     * 是否忽略SSL证书检验
     */
    public boolean ignoreSSL;

    public KmsRuntimeOptions() {
    }

    public KmsRuntimeOptions(com.aliyun.teautil.models.RuntimeOptions runtimeOptions) {
        this.autoretry = runtimeOptions.autoretry;
        this.backoffPeriod = runtimeOptions.backoffPeriod;
        this.backoffPolicy = runtimeOptions.backoffPolicy;
        this.connectTimeout = runtimeOptions.connectTimeout;
        this.httpProxy = runtimeOptions.httpProxy;
        this.httpsProxy = runtimeOptions.httpsProxy;
        this.localAddr = runtimeOptions.localAddr;
        this.maxAttempts = runtimeOptions.maxAttempts;
        this.maxIdleConns = runtimeOptions.maxIdleConns;
        this.noProxy = runtimeOptions.noProxy;
        this.readTimeout = runtimeOptions.readTimeout;
        this.socks5NetWork = runtimeOptions.socks5NetWork;
        this.socks5Proxy = runtimeOptions.socks5Proxy;
    }

    public Boolean getIsUseKmsShareGateway() {
        return this.isUseKmsShareGateway;
    }

    public KmsRuntimeOptions setIsUseKmsShareGateway(Boolean useKms) {
        this.isUseKmsShareGateway = useKms;
        return this;
    }

    public Charset getCharset() {
        return charset;
    }

    public KmsRuntimeOptions setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public boolean getIgnoreSSL() {
        return ignoreSSL;
    }

    public KmsRuntimeOptions setIgnoreSSL(boolean ignoreSSL) {
        this.ignoreSSL = ignoreSSL;
        return this;
    }
}
