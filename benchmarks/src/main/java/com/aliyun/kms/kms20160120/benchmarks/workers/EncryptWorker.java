package com.aliyun.kms.kms20160120.benchmarks.workers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.models.EncryptRequest;
import com.aliyun.dkms.gcs.sdk.models.EncryptResponse;
import com.aliyun.kms.kms20160120.Client;

public class EncryptWorker implements Worker {
    private final Client client;
    private final byte[] plainText;
    private final String keyId;
    private final byte[] aad;

    public EncryptWorker(Client client, String keyId, byte[] plainText, byte[] aad) {
        this.client = client;
        this.keyId = keyId;
        this.plainText = plainText;
        this.aad = aad;
    }

    @Override
    public String doAction() throws Exception {
        return this.encrypt().getRequestId();
    }

    public EncryptResponse encrypt() throws Exception {
        EncryptRequest request = new EncryptRequest();
        request.setKeyId(this.keyId);
        request.setPlaintext(this.plainText);
        request.setAad(this.aad);
        RuntimeOptions runtimeOptions = new RuntimeOptions();
        if(this.client._ca == null || this.client._ca.isEmpty()){
            runtimeOptions.setIgnoreSSL(true);
        }
        return client.encryptWithOptions(request, runtimeOptions);
    }
}
