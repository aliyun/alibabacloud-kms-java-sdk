package com.aliyun.kms.kms20160120.benchmarks.workers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.models.SignRequest;
import com.aliyun.dkms.gcs.sdk.models.SignResponse;
import com.aliyun.kms.kms20160120.Client;

public class SignWorker implements Worker {
    private final Client client;
    private final String keyId;
    private final String messageType;
    private final byte[] message;

    public SignWorker(Client client, String keyId, byte[] message, String messageType) {
        this.client = client;
        this.keyId = keyId;
        this.message = message;
        this.messageType = messageType;
    }

    @Override
    public String doAction() throws Exception {
        return this.sign().getRequestId();
    }

    public SignResponse sign() throws Exception {
        SignRequest request = new SignRequest();
        request.setKeyId(this.keyId);
        request.setMessage(this.message);
        request.setMessageType(this.messageType);
        RuntimeOptions runtimeOptions = new RuntimeOptions();
        if(this.client._ca == null || this.client._ca.isEmpty()){
            runtimeOptions.setIgnoreSSL(true);
        }
        return this.client.signWithOptions(request, runtimeOptions);
    }

}
