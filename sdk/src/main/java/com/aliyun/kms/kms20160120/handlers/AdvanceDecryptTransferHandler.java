package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.Client;
import com.aliyun.dkms.gcs.sdk.models.AdvanceDecryptRequest;
import com.aliyun.dkms.gcs.sdk.models.AdvanceDecryptResponse;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.ArrayUtils;
import com.aliyun.kms.kms20160120.utils.Constants;
import com.aliyun.kms.kms20160120.utils.EncryptionContextUtils;
import com.aliyun.kms20160120.models.DecryptResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaModel;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.OpenApiRequest;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Map;

public class AdvanceDecryptTransferHandler implements KmsTransferHandler<AdvanceDecryptRequest, AdvanceDecryptResponse> {

    private Client client;
    private KmsConfig kmsConfig;
    private String action;

    public AdvanceDecryptTransferHandler(Client client, KmsConfig kmsConfig, String action) {
        this.client = client;
        this.kmsConfig = kmsConfig;
        this.action = action;
    }


    @Override
    public <T extends TeaModel> AdvanceDecryptRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> query = request.getQuery();
        if (StringUtils.isEmpty(query.get("CiphertextBlob"))) {
            throw newMissingParameterClientException("CiphertextBlob");
        }
        AdvanceDecryptRequest decryptDKmsRequest = new AdvanceDecryptRequest();
        byte[] ciphertextBlob = base64.decode(query.get("CiphertextBlob"));
        byte[] ivBytes = Arrays.copyOfRange(ciphertextBlob, Constants.EKT_ID_LENGTH, Constants.EKT_ID_LENGTH + Constants.GCM_IV_LENGTH);
        byte cipherVerAndPaddingMode = Constants.CIPHER_VER << 4 | 0;
        decryptDKmsRequest.setIv(ivBytes);
        decryptDKmsRequest.setCiphertextBlob(ArrayUtils.concatAll(new byte[]{Constants.MAGIC_NUM, cipherVerAndPaddingMode, Constants.ALG_AES_GCM}, ciphertextBlob));
        String encryptionContext = query.get("EncryptionContext");
        if (!StringUtils.isEmpty(encryptionContext)) {
            decryptDKmsRequest.setAad(EncryptionContextUtils.sortAndEncode(encryptionContext, runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()));
        }
        return decryptDKmsRequest;
    }

    @Override
    public AdvanceDecryptResponse callDKMS(AdvanceDecryptRequest request, KmsRuntimeOptions runtimeOptions) throws Exception {
        RuntimeOptions dkmsRuntimeOptions = transferRuntimeOptions(runtimeOptions);
        dkmsRuntimeOptions.setResponseHeaders(responseHeaders);
        return client.advanceDecryptWithOptions(request, dkmsRuntimeOptions);
    }

    @Override
    public Map<String, ?> transferToOpenApiResponse(AdvanceDecryptResponse response, KmsRuntimeOptions runtimeOptions) throws TeaException {
        DecryptResponseBody body = new DecryptResponseBody();
        final com.aliyun.kms20160120.models.DecryptResponse decryptKmsResponse = new com.aliyun.kms20160120.models.DecryptResponse();
        body.setKeyId(response.getKeyId());
        body.setKeyVersionId(response.getKeyVersionId());
        body.setPlaintext(new String(response.getPlaintext(), runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()));
        body.setRequestId(response.getRequestId());
        decryptKmsResponse.setStatusCode(HttpURLConnection.HTTP_OK);
        decryptKmsResponse.setHeaders(response.getResponseHeaders());
        decryptKmsResponse.setBody(body);
        return decryptKmsResponse.toMap();
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
