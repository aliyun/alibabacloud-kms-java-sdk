package com.aliyun.kms.kms20160120.benchmarks.workers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.models.GenerateDataKeyPairRequest;
import com.aliyun.dkms.gcs.sdk.models.GenerateDataKeyPairResponse;
import com.aliyun.kms.kms20160120.Client;

public class GenerateDataKeyPairKeyWorker implements Worker {
    private final Client client;
    private final String keyId;
    private final String keyFormat;
    private final String keyPairSpec;
    private final String algorithm;
    private final byte[] aad;

    public GenerateDataKeyPairKeyWorker(Client client, String keyId, String keyFormat, String keyPairSpec, String algorithm, byte[] aad) {
        this.client = client;
        this.keyId = keyId;
        this.keyFormat = keyFormat;
        this.keyPairSpec = keyPairSpec;
        this.algorithm = algorithm;
        this.aad = aad;
    }

    @Override
    public String doAction() throws Exception {
        return this.generateDataKeyPair().getRequestId();
    }

    public GenerateDataKeyPairResponse generateDataKeyPair() throws Exception {
        GenerateDataKeyPairRequest request = new GenerateDataKeyPairRequest()
                .setKeyFormat(this.keyFormat)
                .setAad(this.aad)
                .setKeyId(this.keyId)
                .setKeyPairSpec(this.keyPairSpec)
                .setAlgorithm(this.algorithm);
        RuntimeOptions runtimeOptions = new RuntimeOptions();
        if (this.client._ca == null || this.client._ca.isEmpty()) {
            runtimeOptions.setIgnoreSSL(true);
        }
        runtimeOptions.setAutoretry(false);
        return client.generateDataKeyPairWithOptions(request, runtimeOptions);
    }
}
