package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.Client;
import com.aliyun.dkms.gcs.sdk.models.SignRequest;
import com.aliyun.dkms.gcs.sdk.models.SignResponse;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.Constants;
import com.aliyun.kms20160120.models.AsymmetricSignResponse;
import com.aliyun.kms20160120.models.AsymmetricSignResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.OpenApiRequest;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class AsymmetricSignTransferHandler implements KmsTransferHandler<SignRequest, SignResponse> {

    private Client client;
    private KmsConfig kmsConfig;
    private String action;

    public AsymmetricSignTransferHandler(Client client, KmsConfig kmsConfig, String action) {
        this.client = client;
        this.kmsConfig = kmsConfig;
        this.action = action;
    }

    @Override
    public SignRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> query = request.getQuery();
        if (StringUtils.isEmpty(query.get("Digest"))) {
            throw newMissingParameterClientException("Digest");
        }
        SignRequest signDKmsRequest = new SignRequest();
        signDKmsRequest.setKeyId(query.get("KeyId"));
        signDKmsRequest.setAlgorithm(query.get("Algorithm"));
        signDKmsRequest.setMessage(base64.decode(query.get("Digest")));
        signDKmsRequest.setMessageType(Constants.DIGEST_MESSAGE_TYPE);
        final String keyVersionId = query.get("KeyVersionId");
        if(!StringUtils.isEmpty(keyVersionId)) {
            signDKmsRequest.setRequestHeaders(new HashMap<String, String>() {{
                put(Constants.MIGRATION_KEY_VERSION_ID_KEY, keyVersionId);
            }});
        }
        return signDKmsRequest;
    }

    @Override
    public SignResponse callDKMS(SignRequest signRequest, KmsRuntimeOptions runtimeOptions) throws Exception {
        RuntimeOptions dkmsRuntimeOptions = transferRuntimeOptions(runtimeOptions);
        dkmsRuntimeOptions.setResponseHeaders(responseHeaders);
        return client.signWithOptions(signRequest, dkmsRuntimeOptions);
    }

    @Override
    public Map<String, ?> transferToOpenApiResponse(SignResponse signResponse, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> responseHeaders = signResponse.getResponseHeaders();
        String keyVersionId = null;
        if (responseHeaders != null) {
            keyVersionId = responseHeaders.get(Constants.MIGRATION_KEY_VERSION_ID_KEY);
        }
        AsymmetricSignResponseBody body = new AsymmetricSignResponseBody();
        body.setKeyId(signResponse.getKeyId());
        body.setKeyVersionId(keyVersionId);
        body.setValue(base64.encodeToString(signResponse.getSignature()));
        body.setRequestId(signResponse.getRequestId());
        final AsymmetricSignResponse asymmetricSignKmsResponse = new AsymmetricSignResponse();
        asymmetricSignKmsResponse.setBody(body);
        asymmetricSignKmsResponse.setHeaders(responseHeaders);
        asymmetricSignKmsResponse.setStatusCode(HttpURLConnection.HTTP_OK);
        return asymmetricSignKmsResponse.toMap();
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
