package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.Client;
import com.aliyun.dkms.gcs.sdk.models.EncryptRequest;
import com.aliyun.dkms.gcs.sdk.models.EncryptResponse;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.Constants;
import com.aliyun.kms20160120.models.AsymmetricEncryptResponse;
import com.aliyun.kms20160120.models.AsymmetricEncryptResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.OpenApiRequest;

import java.net.HttpURLConnection;
import java.util.Map;

public class AsymmetricEncryptTransferHandler implements KmsTransferHandler<EncryptRequest, EncryptResponse> {

    private Client client;
    private KmsConfig kmsConfig;
    private String action;

    public AsymmetricEncryptTransferHandler(Client client, KmsConfig kmsConfig, String action) {
        this.client = client;
        this.kmsConfig = kmsConfig;
        this.action = action;
    }


    @Override
    public EncryptRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> query = request.getQuery();
        if (StringUtils.isEmpty(query.get("Plaintext"))) {
            throw newMissingParameterClientException("Plaintext");
        }
        EncryptRequest asymmetricEncryptDKmsRequest = new EncryptRequest();
        asymmetricEncryptDKmsRequest.setKeyId(query.get("KeyId"));
        asymmetricEncryptDKmsRequest.setPlaintext(base64.decode(query.get("Plaintext")));
        asymmetricEncryptDKmsRequest.setAlgorithm(query.get("Algorithm"));
        return asymmetricEncryptDKmsRequest;
    }

    @Override
    public EncryptResponse callDKMS(EncryptRequest encryptRequest, KmsRuntimeOptions runtimeOptions) throws Exception {
        RuntimeOptions dkmsRuntimeOptions = transferRuntimeOptions(runtimeOptions);
        dkmsRuntimeOptions.setResponseHeaders(responseHeaders);
        return client.encryptWithOptions(encryptRequest, dkmsRuntimeOptions);
    }

    @Override
    public Map<String, ?> transferToOpenApiResponse(EncryptResponse encryptResponse, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> responseHeaders = encryptResponse.getResponseHeaders();
        String keyVersionId = null;
        if (responseHeaders != null) {
            keyVersionId = responseHeaders.get(Constants.MIGRATION_KEY_VERSION_ID_KEY);
        }
        AsymmetricEncryptResponseBody body = new AsymmetricEncryptResponseBody();
        body.setKeyId(encryptResponse.getKeyId());
        body.setKeyVersionId(keyVersionId);
        body.setCiphertextBlob(base64.encodeToString(encryptResponse.getCiphertextBlob()));
        body.setRequestId(encryptResponse.getRequestId());
        final AsymmetricEncryptResponse asymmetricEncryptKmsResponse = new AsymmetricEncryptResponse();
        asymmetricEncryptKmsResponse.setBody(body);
        asymmetricEncryptKmsResponse.setStatusCode(HttpURLConnection.HTTP_OK);
        asymmetricEncryptKmsResponse.setHeaders(responseHeaders);
        return asymmetricEncryptKmsResponse.toMap();
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
