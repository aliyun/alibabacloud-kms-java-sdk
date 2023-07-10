package com.aliyun.kms.kms20160120.benchmarks.workers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.models.VerifyRequest;
import com.aliyun.dkms.gcs.sdk.models.VerifyResponse;
import com.aliyun.kms.kms20160120.Client;

public class VerifyWorker implements Worker{
    private final Client client;
    private final String keyId;
    private final String messageType;
    private final byte[] message;
    private final byte[] signature;

    public VerifyWorker(Client client, String keyId,  byte[] message,String messageType, byte[] signature){
        this.client = client;
        this.keyId = keyId;
        this.message = message;
        this.messageType = messageType;
        this.signature = signature;
    }

    @Override
    public String doAction() throws Exception {
        return this.verify().getRequestId();
    }

    public VerifyResponse verify() throws Exception {
        VerifyRequest request = new VerifyRequest();
        request.setKeyId(this.keyId);
        request.setMessage(this.message);
        request.setMessageType(this.messageType);
        request.setSignature(this.signature);
        RuntimeOptions runtimeOptions = new RuntimeOptions();
        if(this.client._ca == null || this.client._ca.isEmpty()){
            runtimeOptions.setIgnoreSSL(true);
        }
        return this.client.verifyWithOptions(request, runtimeOptions);
    }
}
