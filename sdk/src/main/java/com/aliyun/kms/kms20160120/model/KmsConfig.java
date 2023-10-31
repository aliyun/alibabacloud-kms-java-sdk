package com.aliyun.kms.kms20160120.model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KmsConfig extends com.aliyun.dkms.gcs.openapi.models.Config {

    /**
     * 默认使用KMS共享网关的接口API Name列表
     */
    private List<String> defaultKmsApiNames = new ArrayList<>();
    /**
     * 指定所有接口使用到的字符集编码
     */
    private Charset charset = StandardCharsets.UTF_8;
    /**
     * ssl验证开关,默认为false,即需验证ssl证书;为true时,表示可在调用接口时设置是否忽略ssl证书
     */
    private boolean ignoreSSLVerifySwitch;
    @Deprecated
    private boolean advanceSwitch = true;
    /**
     * 强制使用低级转换接口 默认是false,即使用高级转换接口
     */
    private boolean forceLowVersionCryptoTransfer;

    public KmsConfig() {
    }


    public List<String> getDefaultKmsApiNames() {
        return defaultKmsApiNames;
    }

    public KmsConfig setDefaultKmsApiNames(String... defaultKmsApiNames) {
        this.defaultKmsApiNames.addAll(Arrays.asList(defaultKmsApiNames));
        return this;
    }

    public Charset getCharset() {
        return charset;
    }

    public KmsConfig setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public boolean getIgnoreSSLVerifySwitch() {
        return ignoreSSLVerifySwitch;
    }

    public KmsConfig setIgnoreSSLVerifySwitch(boolean ignoreSSLVerifySwitch) {
        this.ignoreSSLVerifySwitch = ignoreSSLVerifySwitch;
        return this;
    }

    @Deprecated
    public boolean getAdvanceSwitch() {
        return advanceSwitch;
    }

    @Deprecated
    public KmsConfig setAdvanceSwitch(boolean advanceSwitch) {
        this.advanceSwitch = advanceSwitch;
        return this;
    }

    public boolean getForceLowVersionCryptoTransfer() {
        return forceLowVersionCryptoTransfer;
    }

    public KmsConfig setForceLowVersionCryptoTransfer(boolean forceLowVersionCryptoTransfer) {
        this.forceLowVersionCryptoTransfer = forceLowVersionCryptoTransfer;
        return this;
    }

}
