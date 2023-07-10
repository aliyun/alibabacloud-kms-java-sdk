package com.aliyun.kms.kms20160120.benchmarks.workers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.models.GetSecretValueRequest;
import com.aliyun.dkms.gcs.sdk.models.GetSecretValueResponse;
import com.aliyun.kms.kms20160120.Client;

public class GetSecretValueWorker implements Worker {
    private final Client client;
    private final String secretName;

    public GetSecretValueWorker(Client client, String secretName){
        this.client = client;
        this.secretName = secretName;
    }

    @Override
    public String doAction() throws Exception {
        return this.getSecretValue().getRequestId();
    }

    public GetSecretValueResponse getSecretValue() throws Exception {
        GetSecretValueRequest request = new GetSecretValueRequest();
        request.setSecretName(this.secretName);
        RuntimeOptions runtimeOptions = new RuntimeOptions();
        if(this.client._ca == null || this.client._ca.isEmpty()){
            runtimeOptions.setIgnoreSSL(true);
        }
        return this.client.getSecretValueWithOptions(request, runtimeOptions);
    }

}
