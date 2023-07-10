package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.Client;
import com.aliyun.dkms.gcs.sdk.models.*;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.ArrayUtils;
import com.aliyun.kms.kms20160120.utils.Constants;
import com.aliyun.kms.kms20160120.utils.EncryptionContextUtils;
import com.aliyun.kms20160120.models.GenerateDataKeyWithoutPlaintextResponse;
import com.aliyun.kms20160120.models.GenerateDataKeyWithoutPlaintextResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.OpenApiRequest;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.aliyun.kms.kms20160120.utils.Constants.*;
import static com.aliyun.kms.kms20160120.utils.KmsErrorCodeTransferUtils.INVALID_PARAMETER_ERROR_CODE;
import static com.aliyun.kms.kms20160120.utils.KmsErrorCodeTransferUtils.INVALID_PARAMETER_KEY_SPEC_ERROR_MESSAGE;

public class AdvanceGenerateDataKeyWithoutPlaintextTransferHandler implements KmsTransferHandler<AdvanceGenerateDataKeyRequest, AdvanceGenerateDataKeyResponse> {
    private Client client;
    private KmsConfig kmsConfig;
    private String action;

    public AdvanceGenerateDataKeyWithoutPlaintextTransferHandler(Client client, KmsConfig kmsConfig, String action) {
        this.client = client;
        this.kmsConfig = kmsConfig;
        this.action = action;
    }

    @Override
    public AdvanceGenerateDataKeyRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> query = request.getQuery();
        AdvanceGenerateDataKeyRequest generateDataKeyDKmsRequest = new AdvanceGenerateDataKeyRequest();
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
        String encryptionContext = query.get("EncryptionContext");
        if (!StringUtils.isEmpty(encryptionContext)) {
            generateDataKeyDKmsRequest.setAad(EncryptionContextUtils.sortAndEncode(encryptionContext, runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()));
        }
        return generateDataKeyDKmsRequest;
    }


    @Override
    public AdvanceGenerateDataKeyResponse callDKMS(AdvanceGenerateDataKeyRequest request, KmsRuntimeOptions runtimeOptions) throws Exception {
        RuntimeOptions dkmsRuntimeOptions = transferRuntimeOptions(runtimeOptions);
        dkmsRuntimeOptions.setResponseHeaders(responseHeaders);
        AdvanceGenerateDataKeyResponse generateDataKeyResponse = client.advanceGenerateDataKeyWithOptions(request, dkmsRuntimeOptions);
        AdvanceEncryptRequest advanceEncryptRequest = new AdvanceEncryptRequest();
        advanceEncryptRequest.setKeyId(request.getKeyId());
        advanceEncryptRequest.setPlaintext(base64.encodeAsString(generateDataKeyResponse.getPlaintext()).getBytes(runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()));
        advanceEncryptRequest.setAad(request.getAad());
        AdvanceEncryptResponse advanceEncryptResponse = client.advanceEncryptWithOptions(advanceEncryptRequest, dkmsRuntimeOptions);
        generateDataKeyResponse.setCiphertextBlob(advanceEncryptResponse.getCiphertextBlob());
        generateDataKeyResponse.setIv(advanceEncryptResponse.getIv());
        return generateDataKeyResponse;
    }

    @Override
    public Map<String, ?> transferToOpenApiResponse(AdvanceGenerateDataKeyResponse response, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> responseHeaders = response.getResponseHeaders();
        int from = MAGIC_NUM_LENGTH + CIPHER_VER_AND_PADDING_MODE_LENGTH + ALGORITHM_LENGTH;
        byte[] ciphertextBlob = Arrays.copyOfRange(response.getCiphertextBlob(), from, response.getCiphertextBlob().length);
        GenerateDataKeyWithoutPlaintextResponseBody body = new GenerateDataKeyWithoutPlaintextResponseBody();
        body.setKeyId(response.getKeyId());
        body.setKeyVersionId(response.getKeyVersionId());
        body.setRequestId(response.getRequestId());
        body.setCiphertextBlob(base64.encodeToString(ciphertextBlob));
        final GenerateDataKeyWithoutPlaintextResponse generateDataKeyWithoutPlaintextKmsResponse = new GenerateDataKeyWithoutPlaintextResponse();
        generateDataKeyWithoutPlaintextKmsResponse.setBody(body);
        generateDataKeyWithoutPlaintextKmsResponse.setHeaders(responseHeaders);
        generateDataKeyWithoutPlaintextKmsResponse.setStatusCode(HttpURLConnection.HTTP_OK);
        return generateDataKeyWithoutPlaintextKmsResponse.toMap();
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
