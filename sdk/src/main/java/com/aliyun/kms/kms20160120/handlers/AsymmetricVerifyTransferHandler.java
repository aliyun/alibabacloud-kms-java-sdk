package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.Client;
import com.aliyun.dkms.gcs.sdk.models.VerifyRequest;
import com.aliyun.dkms.gcs.sdk.models.VerifyResponse;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.Constants;
import com.aliyun.kms20160120.models.AsymmetricVerifyResponse;
import com.aliyun.kms20160120.models.AsymmetricVerifyResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.OpenApiRequest;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class AsymmetricVerifyTransferHandler implements KmsTransferHandler<VerifyRequest, VerifyResponse> {

    private Client client;
    private KmsConfig kmsConfig;
    private String action;

    public AsymmetricVerifyTransferHandler(Client client, KmsConfig kmsConfig, String action) {
        this.client = client;
        this.kmsConfig = kmsConfig;
        this.action = action;
    }

    @Override
    public VerifyRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> query = request.getQuery();
        if (StringUtils.isEmpty(query.get("Digest"))) {
            throw newMissingParameterClientException("Digest");
        }
        if (StringUtils.isEmpty(query.get("Value"))) {
            throw newMissingParameterClientException("Value");
        }
        VerifyRequest verifyDKmsRequest = new VerifyRequest();
        verifyDKmsRequest.setKeyId(query.get("KeyId"));
        verifyDKmsRequest.setAlgorithm(query.get("Algorithm"));
        verifyDKmsRequest.setMessage(base64.decode(query.get("Digest")));
        verifyDKmsRequest.setMessageType(Constants.DIGEST_MESSAGE_TYPE);
        verifyDKmsRequest.setSignature(base64.decode(query.get("Value")));
        final String keyVersionId = query.get("KeyVersionId");
        if(!StringUtils.isEmpty(keyVersionId)) {
            verifyDKmsRequest.setRequestHeaders(new HashMap<String, String>() {{
                put(Constants.MIGRATION_KEY_VERSION_ID_KEY, keyVersionId);
            }});
        }
        return verifyDKmsRequest;
    }

    @Override
    public VerifyResponse callDKMS(VerifyRequest verifyRequest, KmsRuntimeOptions runtimeOptions) throws Exception {
        RuntimeOptions dkmsRuntimeOptions = transferRuntimeOptions(runtimeOptions);
        dkmsRuntimeOptions.setResponseHeaders(responseHeaders);
        return client.verifyWithOptions(verifyRequest, dkmsRuntimeOptions);
    }

    @Override
    public Map<String, ?> transferToOpenApiResponse(VerifyResponse verifyResponse, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> responseHeaders = verifyResponse.getResponseHeaders();
        String keyVersionId = null;
        if (responseHeaders != null) {
            keyVersionId = responseHeaders.get(Constants.MIGRATION_KEY_VERSION_ID_KEY);
        }
        AsymmetricVerifyResponseBody body = new AsymmetricVerifyResponseBody();
        body.setKeyId(verifyResponse.getKeyId());
        body.setKeyVersionId(keyVersionId);
        body.setValue(verifyResponse.getValue());
        body.setRequestId(verifyResponse.getRequestId());
        final AsymmetricVerifyResponse asymmetricVerifyKmsResponse = new AsymmetricVerifyResponse();
        asymmetricVerifyKmsResponse.setBody(body);
        asymmetricVerifyKmsResponse.setStatusCode(HttpURLConnection.HTTP_OK);
        asymmetricVerifyKmsResponse.setHeaders(responseHeaders);
        return asymmetricVerifyKmsResponse.toMap();
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
