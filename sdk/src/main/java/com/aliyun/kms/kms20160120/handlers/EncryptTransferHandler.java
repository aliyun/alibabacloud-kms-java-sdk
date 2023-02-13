package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.Client;
import com.aliyun.dkms.gcs.sdk.models.EncryptRequest;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.ArrayUtils;
import com.aliyun.kms.kms20160120.utils.Constants;
import com.aliyun.kms20160120.models.EncryptResponse;
import com.aliyun.kms20160120.models.EncryptResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.OpenApiRequest;

import java.net.HttpURLConnection;
import java.util.Map;

public class EncryptTransferHandler implements KmsTransferHandler<com.aliyun.dkms.gcs.sdk.models.EncryptRequest, com.aliyun.dkms.gcs.sdk.models.EncryptResponse> {

    private Client client;
    private KmsConfig kmsConfig;
    private String action;

    public EncryptTransferHandler(Client client, KmsConfig kmsConfig, String action) {
        this.client = client;
        this.kmsConfig = kmsConfig;
        this.action = action;
    }


    @Override
    public com.aliyun.dkms.gcs.sdk.models.EncryptRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> query = request.getQuery();
        if (StringUtils.isEmpty(query.get("Plaintext"))) {
            throw newMissingParameterClientException("Plaintext");
        }
        com.aliyun.dkms.gcs.sdk.models.EncryptRequest encryptDKmsRequest = new com.aliyun.dkms.gcs.sdk.models.EncryptRequest();
        encryptDKmsRequest.setKeyId(query.get("KeyId"));
        encryptDKmsRequest.setPlaintext(query.get("Plaintext").getBytes(runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()));
        if (!StringUtils.isEmpty(query.get("EncryptionContext"))) {
            encryptDKmsRequest.setAad(query.get("EncryptionContext").getBytes(runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()));
        }
        return encryptDKmsRequest;
    }

    @Override
    public com.aliyun.dkms.gcs.sdk.models.EncryptResponse callDKMS(EncryptRequest encryptRequest, KmsRuntimeOptions runtimeOptions) throws Exception {
        RuntimeOptions dkmsRuntimeOptions = transferRuntimeOptions(runtimeOptions);
        dkmsRuntimeOptions.setResponseHeaders(responseHeaders);
        return this.client.encryptWithOptions(encryptRequest, dkmsRuntimeOptions);
    }

    @Override
    public Map<String, ?> transferToOpenApiResponse(com.aliyun.dkms.gcs.sdk.models.EncryptResponse response, KmsRuntimeOptions runtimeOptions) throws TeaException {
        EncryptResponseBody body = new EncryptResponseBody();
        body.setKeyId(response.getKeyId());
        Map<String, String> responseHeaders = response.getResponseHeaders();
        String keyVersionId;
        if (responseHeaders == null || responseHeaders.size() == 0 || StringUtils.isEmpty(keyVersionId = responseHeaders.get(Constants.MIGRATION_KEY_VERSION_ID_KEY))) {
            throw new RuntimeException(String.format("Can not found response headers parameter[%s]", Constants.MIGRATION_KEY_VERSION_ID_KEY));
        }
        byte[] ciphertextBlob = ArrayUtils.concatAll(keyVersionId.getBytes(runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()), response.getIv(), response.getCiphertextBlob());
        body.setKeyVersionId(keyVersionId);
        body.setCiphertextBlob(base64.encodeToString(ciphertextBlob));
        body.setRequestId(response.getRequestId());
        EncryptResponse encryptResponse = new EncryptResponse();
        encryptResponse.setBody(body);
        encryptResponse.setStatusCode(HttpURLConnection.HTTP_OK);
        encryptResponse.setHeaders(responseHeaders);
        return encryptResponse.toMap();
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
