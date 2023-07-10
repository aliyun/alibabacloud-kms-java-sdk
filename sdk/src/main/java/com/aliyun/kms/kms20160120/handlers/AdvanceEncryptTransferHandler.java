package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.Client;
import com.aliyun.dkms.gcs.sdk.models.AdvanceEncryptRequest;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.ArrayUtils;
import com.aliyun.kms.kms20160120.utils.EncryptionContextUtils;
import com.aliyun.kms20160120.models.EncryptResponse;
import com.aliyun.kms20160120.models.EncryptResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.OpenApiRequest;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Map;

import static com.aliyun.kms.kms20160120.utils.Constants.*;

public class AdvanceEncryptTransferHandler implements KmsTransferHandler<AdvanceEncryptRequest, com.aliyun.dkms.gcs.sdk.models.AdvanceEncryptResponse> {

    private Client client;
    private KmsConfig kmsConfig;
    private String action;

    public AdvanceEncryptTransferHandler(Client client, KmsConfig kmsConfig, String action) {
        this.client = client;
        this.kmsConfig = kmsConfig;
        this.action = action;
    }


    @Override
    public AdvanceEncryptRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> query = request.getQuery();
        if (StringUtils.isEmpty(query.get("Plaintext"))) {
            throw newMissingParameterClientException("Plaintext");
        }
        com.aliyun.dkms.gcs.sdk.models.AdvanceEncryptRequest advanceEncryptRequestDKmsRequest = new com.aliyun.dkms.gcs.sdk.models.AdvanceEncryptRequest();
        advanceEncryptRequestDKmsRequest.setKeyId(query.get("KeyId"));
        advanceEncryptRequestDKmsRequest.setPlaintext(query.get("Plaintext").getBytes(runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()));
        String encryptionContext = query.get("EncryptionContext");
        if (!StringUtils.isEmpty(encryptionContext)) {
            advanceEncryptRequestDKmsRequest.setAad(EncryptionContextUtils.sortAndEncode(encryptionContext, runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()));
        }
        return advanceEncryptRequestDKmsRequest;
    }

    @Override
    public com.aliyun.dkms.gcs.sdk.models.AdvanceEncryptResponse callDKMS(AdvanceEncryptRequest request, KmsRuntimeOptions runtimeOptions) throws Exception {
        RuntimeOptions dkmsRuntimeOptions = transferRuntimeOptions(runtimeOptions);
        dkmsRuntimeOptions.setResponseHeaders(responseHeaders);
        return this.client.advanceEncryptWithOptions(request, dkmsRuntimeOptions);
    }

    @Override
    public Map<String, ?> transferToOpenApiResponse(com.aliyun.dkms.gcs.sdk.models.AdvanceEncryptResponse response, KmsRuntimeOptions runtimeOptions) throws TeaException {
        EncryptResponseBody body = new EncryptResponseBody();
        body.setKeyId(response.getKeyId());
        int from = MAGIC_NUM_LENGTH + CIPHER_VER_AND_PADDING_MODE_LENGTH + ALGORITHM_LENGTH;
        byte[] ciphertextBlob = Arrays.copyOfRange(response.getCiphertextBlob(), from, response.getCiphertextBlob().length);
        body.setKeyVersionId(response.getKeyVersionId());
        body.setCiphertextBlob(base64.encodeToString(ciphertextBlob));
        body.setRequestId(response.getRequestId());
        EncryptResponse encryptResponse = new EncryptResponse();
        encryptResponse.setBody(body);
        encryptResponse.setStatusCode(HttpURLConnection.HTTP_OK);
        encryptResponse.setHeaders(response.getResponseHeaders());
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
