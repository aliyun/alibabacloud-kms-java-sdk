package com.aliyun.kms.kms20160120.benchmarks.workers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.models.DecryptRequest;
import com.aliyun.dkms.gcs.sdk.models.DecryptResponse;
import com.aliyun.kms.kms20160120.Client;

public class DecryptWorker implements Worker{
    private final Client client;
    private final String keyId;
    private final byte[] cipher;
    private final byte[] iv;
    private final byte[] aad;

    public DecryptWorker(Client client, String keyId, byte[] cipher, byte[] iv, byte[] aad){
        this.client = client;
        this.keyId = keyId;
        this.cipher = cipher;
        this.iv = iv;
        this.aad = aad;
    }

    @Override
    public String doAction() throws Exception {
        return this.decrypt().getRequestId();
    }

    public DecryptResponse decrypt() throws Exception {
        DecryptRequest request = new DecryptRequest();
        request.setKeyId(this.keyId);
        request.setCiphertextBlob(this.cipher);
        request.setIv(this.iv);
        request.setAad(this.aad);
        RuntimeOptions runtimeOptions = new RuntimeOptions();
        if(this.client._ca == null || this.client._ca.isEmpty()){
            runtimeOptions.setIgnoreSSL(true);
        }
        return this.client.decryptWithOptions(request, runtimeOptions);
    }
}
