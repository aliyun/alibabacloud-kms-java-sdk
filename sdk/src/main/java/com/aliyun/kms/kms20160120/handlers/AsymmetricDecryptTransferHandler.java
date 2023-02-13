package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.Client;
import com.aliyun.dkms.gcs.sdk.models.DecryptRequest;
import com.aliyun.dkms.gcs.sdk.models.DecryptResponse;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.Constants;
import com.aliyun.kms20160120.models.AsymmetricDecryptResponse;
import com.aliyun.kms20160120.models.AsymmetricDecryptResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.OpenApiRequest;

import java.net.HttpURLConnection;
import java.util.Map;

public class AsymmetricDecryptTransferHandler implements KmsTransferHandler<com.aliyun.dkms.gcs.sdk.models.DecryptRequest, com.aliyun.dkms.gcs.sdk.models.DecryptResponse> {


    private Client client;
    private KmsConfig kmsConfig;
    private String action;

    public AsymmetricDecryptTransferHandler(Client client, KmsConfig kmsConfig, String action) {
        this.client = client;
        this.kmsConfig = kmsConfig;
        this.action = action;
    }

    @Override
    public DecryptRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> query = request.getQuery();
        if (StringUtils.isEmpty(query.get("CiphertextBlob"))) {
            throw newMissingParameterClientException("CiphertextBlob");
        }
        DecryptRequest decryptRequest = new DecryptRequest();
        decryptRequest.setAlgorithm(query.get("Algorithm"));
        decryptRequest.setCiphertextBlob(base64.decode(query.get("CiphertextBlob")));
        decryptRequest.setKeyId(query.get("KeyId"));
        return decryptRequest;
    }

    @Override
    public DecryptResponse callDKMS(DecryptRequest decryptRequest, KmsRuntimeOptions runtimeOptions) throws Exception {
        RuntimeOptions dkmsRuntimeOptions = transferRuntimeOptions(runtimeOptions);
        dkmsRuntimeOptions.setResponseHeaders(responseHeaders);
        return client.decryptWithOptions(decryptRequest, dkmsRuntimeOptions);
    }

    @Override
    public Map<String, ?> transferToOpenApiResponse(DecryptResponse decryptResponse, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> responseHeaders = decryptResponse.getResponseHeaders();
        String keyVersionId = null;
        if (responseHeaders != null) {
            keyVersionId = responseHeaders.get(Constants.MIGRATION_KEY_VERSION_ID_KEY);
        }
        AsymmetricDecryptResponseBody body = new AsymmetricDecryptResponseBody();
        body.setKeyId(decryptResponse.getKeyId());
        body.setKeyVersionId(keyVersionId);
        body.setPlaintext(base64.encodeToString(decryptResponse.getPlaintext()));
        AsymmetricDecryptResponse asymmetricDecryptResponse = new AsymmetricDecryptResponse();
        asymmetricDecryptResponse.setBody(body);
        asymmetricDecryptResponse.setStatusCode(HttpURLConnection.HTTP_OK);
        asymmetricDecryptResponse.setHeaders(responseHeaders);
        return asymmetricDecryptResponse.toMap();
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
