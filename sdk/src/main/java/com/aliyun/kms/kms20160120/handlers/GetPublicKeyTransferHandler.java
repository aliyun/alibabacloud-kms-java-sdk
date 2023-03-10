package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.Client;
import com.aliyun.dkms.gcs.sdk.models.GetPublicKeyRequest;
import com.aliyun.dkms.gcs.sdk.models.GetPublicKeyResponse;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.Constants;
import com.aliyun.kms20160120.models.GetPublicKeyResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaModel;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.OpenApiRequest;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class GetPublicKeyTransferHandler implements KmsTransferHandler<GetPublicKeyRequest, GetPublicKeyResponse> {
    private Client client;
    private String action;

    public GetPublicKeyTransferHandler(Client client, KmsConfig kmsConfig, String action) {
        this.client = client;
        this.action = action;
    }

    @Override
    public <T extends TeaModel> GetPublicKeyRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> query = request.getQuery();
        GetPublicKeyRequest getPublicKeyDKmsRequest = new GetPublicKeyRequest();
        getPublicKeyDKmsRequest.setKeyId(query.get("KeyId"));
        final String keyVersionId = query.get("KeyVersionId");
        if(!StringUtils.isEmpty(keyVersionId)) {
            getPublicKeyDKmsRequest.setRequestHeaders(new HashMap<String, String>() {{
                put(Constants.MIGRATION_KEY_VERSION_ID_KEY, keyVersionId);
            }});
        }
        return getPublicKeyDKmsRequest;
    }

    @Override
    public GetPublicKeyResponse callDKMS(GetPublicKeyRequest getPublicKeyRequest, KmsRuntimeOptions runtimeOptions) throws Exception {
        RuntimeOptions dkmsRuntimeOptions = transferRuntimeOptions(runtimeOptions);
        dkmsRuntimeOptions.setResponseHeaders(responseHeaders);
        return client.getPublicKeyWithOptions(getPublicKeyRequest, dkmsRuntimeOptions);
    }

    @Override
    public Map<String, ?> transferToOpenApiResponse(GetPublicKeyResponse getPublicKeyResponse, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> responseHeaders = getPublicKeyResponse.getResponseHeaders();
        String keyVersionId = null;
        if (responseHeaders != null) {
            keyVersionId = responseHeaders.get(Constants.MIGRATION_KEY_VERSION_ID_KEY);
        }
        GetPublicKeyResponseBody body = new GetPublicKeyResponseBody();
        body.setKeyId(getPublicKeyResponse.getKeyId());
        body.setKeyVersionId(keyVersionId);
        body.setRequestId(getPublicKeyResponse.getRequestId());
        body.setPublicKey(getPublicKeyResponse.getPublicKey());
        final com.aliyun.kms20160120.models.GetPublicKeyResponse getPublicKeyKmsResponse = new com.aliyun.kms20160120.models.GetPublicKeyResponse();
        getPublicKeyKmsResponse.setBody(body);
        getPublicKeyKmsResponse.setHeaders(responseHeaders);
        getPublicKeyKmsResponse.setStatusCode(HttpURLConnection.HTTP_OK);
        return getPublicKeyKmsResponse.toMap();
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
