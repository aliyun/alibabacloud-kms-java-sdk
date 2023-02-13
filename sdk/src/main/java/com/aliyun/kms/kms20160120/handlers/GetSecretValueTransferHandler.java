package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.Client;
import com.aliyun.dkms.gcs.sdk.models.GetSecretValueRequest;
import com.aliyun.dkms.gcs.sdk.models.GetSecretValueResponse;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms20160120.models.GetSecretValueResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.OpenApiRequest;

import java.net.HttpURLConnection;
import java.util.Map;

public class GetSecretValueTransferHandler implements KmsTransferHandler<GetSecretValueRequest, GetSecretValueResponse> {

    private Client client;
    private String action;

    public GetSecretValueTransferHandler(Client client, KmsConfig kmsConfig, String action) {
        this.client = client;
        this.action = action;
    }

    @Override
    public GetSecretValueRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> query = request.getQuery();
        GetSecretValueRequest getSecretValueDKmsRequest = new GetSecretValueRequest();
        getSecretValueDKmsRequest.setSecretName(query.get("SecretName"));
        getSecretValueDKmsRequest.setFetchExtendedConfig(Boolean.parseBoolean(query.getOrDefault("FetchExtendedConfig", "false")));
        getSecretValueDKmsRequest.setVersionId(query.get("VersionId"));
        getSecretValueDKmsRequest.setVersionStage(query.get("VersionStage"));
        return getSecretValueDKmsRequest;
    }


    @Override
    public GetSecretValueResponse callDKMS(GetSecretValueRequest getSecretValueRequest, KmsRuntimeOptions runtimeOptions) throws Exception {
        RuntimeOptions dkmsRuntimeOptions = transferRuntimeOptions(runtimeOptions);
        dkmsRuntimeOptions.setResponseHeaders(responseHeaders);
        return this.client.getSecretValueWithOptions(getSecretValueRequest, dkmsRuntimeOptions);
    }

    @Override
    public Map<String, ?> transferToOpenApiResponse(GetSecretValueResponse getSecretValueResponse, KmsRuntimeOptions runtimeOptions) throws TeaException {
        GetSecretValueResponseBody body = new GetSecretValueResponseBody();
        body.setAutomaticRotation(getSecretValueResponse.getAutomaticRotation());
        body.setCreateTime(getSecretValueResponse.getCreateTime());
        body.setExtendedConfig(getSecretValueResponse.getExtendedConfig());
        body.setLastRotationDate(getSecretValueResponse.getLastRotationDate());
        body.setNextRotationDate(getSecretValueResponse.getNextRotationDate());
        body.setRotationInterval(getSecretValueResponse.getRotationInterval());
        body.setSecretData(getSecretValueResponse.getSecretData());
        body.setSecretDataType(getSecretValueResponse.getSecretDataType());
        body.setSecretName(getSecretValueResponse.getSecretName());
        body.setSecretType(getSecretValueResponse.getSecretType());
        body.setVersionId(getSecretValueResponse.getVersionId());
        GetSecretValueResponseBody.GetSecretValueResponseBodyVersionStages versionStage = new GetSecretValueResponseBody.GetSecretValueResponseBodyVersionStages();
        versionStage.setVersionStage(getSecretValueResponse.getVersionStages());
        body.setVersionStages(versionStage);
        body.setRequestId(getSecretValueResponse.getRequestId());
        com.aliyun.kms20160120.models.GetSecretValueResponse response = new com.aliyun.kms20160120.models.GetSecretValueResponse();
        response.setBody(body);
        response.setHeaders(getSecretValueResponse.getResponseHeaders());
        response.setStatusCode(HttpURLConnection.HTTP_OK);
        return response.toMap();
    }

    @Override
    public com.aliyun.dkms.gcs.openapi.Client getClient() {
        return client;
    }

    @Override
    public String getAction() {
        return action;
    }
}
