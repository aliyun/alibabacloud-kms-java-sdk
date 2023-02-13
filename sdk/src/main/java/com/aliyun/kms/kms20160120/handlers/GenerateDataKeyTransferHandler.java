package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.Client;
import com.aliyun.dkms.gcs.sdk.models.*;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.ArrayUtils;
import com.aliyun.kms.kms20160120.utils.Constants;
import com.aliyun.kms20160120.models.GenerateDataKeyResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.OpenApiRequest;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static com.aliyun.kms.kms20160120.utils.KmsErrorCodeTransferUtils.INVALID_PARAMETER_ERROR_CODE;
import static com.aliyun.kms.kms20160120.utils.KmsErrorCodeTransferUtils.INVALID_PARAMETER_KEY_SPEC_ERROR_MESSAGE;

public class GenerateDataKeyTransferHandler implements KmsTransferHandler<GenerateDataKeyRequest, GenerateDataKeyResponse> {
    private Client client;
    private KmsConfig kmsConfig;
    private String action;

    public GenerateDataKeyTransferHandler(Client client, KmsConfig kmsConfig, String action) {
        this.client = client;
        this.kmsConfig = kmsConfig;
        this.action = action;
    }

    @Override
    public GenerateDataKeyRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> query = request.getQuery();
        com.aliyun.dkms.gcs.sdk.models.GenerateDataKeyRequest generateDataKeyDKmsRequest = new com.aliyun.dkms.gcs.sdk.models.GenerateDataKeyRequest();
        generateDataKeyDKmsRequest.setKeyId(query.get("KeyId"));
        String keySpec = query.get("KeySpec");
        Integer numberOfBytes = null;
        if (query.get("NumberOfBytes") == null) {
            if (StringUtils.isEmpty(keySpec) || Constants.KMS_KEY_PAIR_AES_256.equals(keySpec)) {
                numberOfBytes = Constants.NUMBER_OF_BYTES_AES_256;
            } else if (Constants.KMS_KEY_PAIR_AES_128.equals(keySpec)) {
                numberOfBytes = Constants.NUMBER_OF_BYTES_AES_128;
            } else {
                throw new TeaException(new HashMap<String, Object>() {
                    {
                        put("code", INVALID_PARAMETER_ERROR_CODE);
                        put("message", INVALID_PARAMETER_KEY_SPEC_ERROR_MESSAGE);
                    }
                });
            }
        } else {
            numberOfBytes = Integer.parseInt(query.get("NumberOfBytes"));
        }
        generateDataKeyDKmsRequest.setNumberOfBytes(numberOfBytes);
        if (!StringUtils.isEmpty(query.get("EncryptionContext"))) {
            generateDataKeyDKmsRequest.setAad(query.get("EncryptionContext").getBytes(runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()));
        }
        return generateDataKeyDKmsRequest;
    }


    @Override
    public GenerateDataKeyResponse callDKMS(GenerateDataKeyRequest generateDataKeyRequest, KmsRuntimeOptions runtimeOptions) throws Exception {
        RuntimeOptions dkmsRuntimeOptions = transferRuntimeOptions(runtimeOptions);
        dkmsRuntimeOptions.setResponseHeaders(responseHeaders);
        com.aliyun.dkms.gcs.sdk.models.GenerateDataKeyResponse generateDataKeyResponse = client.generateDataKeyWithOptions(generateDataKeyRequest, dkmsRuntimeOptions);
        EncryptRequest encryptRequest = new EncryptRequest();
        encryptRequest.setKeyId(generateDataKeyRequest.getKeyId());
        encryptRequest.setPlaintext(base64.encodeAsString(generateDataKeyResponse.getPlaintext()).getBytes(runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()));
        EncryptResponse encryptResponse = client.encryptWithOptions(encryptRequest, dkmsRuntimeOptions);
        generateDataKeyResponse.setCiphertextBlob(encryptResponse.getCiphertextBlob());
        generateDataKeyResponse.setIv(encryptResponse.getIv());
        return generateDataKeyResponse;
    }

    @Override
    public Map<String, ?> transferToOpenApiResponse(GenerateDataKeyResponse generateDataKeyResponse, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> responseHeaders = generateDataKeyResponse.getResponseHeaders();
        String keyVersionId;
        if (responseHeaders == null || responseHeaders.size() == 0 || StringUtils.isEmpty(keyVersionId = responseHeaders.get(Constants.MIGRATION_KEY_VERSION_ID_KEY))) {
            throw new RuntimeException(String.format("Can not found response headers parameter[%s]", Constants.MIGRATION_KEY_VERSION_ID_KEY));
        }
        byte[] ciphertextBlob = ArrayUtils.concatAll(keyVersionId.getBytes(runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()), generateDataKeyResponse.getIv(), generateDataKeyResponse.getCiphertextBlob());
        GenerateDataKeyResponseBody body = new GenerateDataKeyResponseBody();
        body.setKeyId(generateDataKeyResponse.getKeyId());
        body.setKeyVersionId(keyVersionId);
        body.setRequestId(generateDataKeyResponse.getRequestId());
        body.setPlaintext(base64.encodeToString(generateDataKeyResponse.getPlaintext()));
        body.setCiphertextBlob(base64.encodeToString(ciphertextBlob));
        final com.aliyun.kms20160120.models.GenerateDataKeyResponse generateDataKeyKmsResponse = new com.aliyun.kms20160120.models.GenerateDataKeyResponse();
        generateDataKeyKmsResponse.setBody(body);
        generateDataKeyKmsResponse.setHeaders(responseHeaders);
        generateDataKeyKmsResponse.setStatusCode(HttpURLConnection.HTTP_OK);
        return generateDataKeyKmsResponse.toMap();
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
