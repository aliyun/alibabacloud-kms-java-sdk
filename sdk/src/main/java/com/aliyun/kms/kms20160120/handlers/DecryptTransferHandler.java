package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions;
import com.aliyun.dkms.gcs.sdk.Client;
import com.aliyun.dkms.gcs.sdk.models.DecryptRequest;
import com.aliyun.dkms.gcs.sdk.models.DecryptResponse;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.Constants;
import com.aliyun.kms.kms20160120.utils.EncryptionContextUtils;
import com.aliyun.kms20160120.models.DecryptResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaModel;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.OpenApiRequest;

import java.net.HttpURLConnection;
import java.util.*;

public class DecryptTransferHandler implements KmsTransferHandler<DecryptRequest, DecryptResponse> {

    private Client client;
    private KmsConfig kmsConfig;
    private String action;

    public DecryptTransferHandler(Client client, KmsConfig kmsConfig, String action) {
        this.client = client;
        this.kmsConfig = kmsConfig;
        this.action = action;
    }


    @Override
    public <T extends TeaModel> DecryptRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        Map<String, String> query = request.getQuery();
        if (StringUtils.isEmpty(query.get("CiphertextBlob"))) {
            throw newMissingParameterClientException("CiphertextBlob");
        }
        DecryptRequest decryptDKmsRequest = new DecryptRequest();
        byte[] ciphertextBlob = base64.decode(query.get("CiphertextBlob"));
        if (ciphertextBlob.length <= Constants.EKT_ID_LENGTH + Constants.GCM_IV_LENGTH) {
            throw newInvalidParameterClientException("CiphertextBlob");
        }
        byte[] ektIdBytes = Arrays.copyOfRange(ciphertextBlob, 0, Constants.EKT_ID_LENGTH);
        byte[] ivBytes = Arrays.copyOfRange(ciphertextBlob, Constants.EKT_ID_LENGTH, Constants.EKT_ID_LENGTH + Constants.GCM_IV_LENGTH);
        byte[] ciphertextBytes = Arrays.copyOfRange(ciphertextBlob, Constants.EKT_ID_LENGTH + Constants.GCM_IV_LENGTH, ciphertextBlob.length);
        String ektId = new String(ektIdBytes, runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset());
        decryptDKmsRequest.setRequestHeaders(new HashMap<String, String>() {{
            put(Constants.MIGRATION_KEY_VERSION_ID_KEY, ektId);
        }});
        decryptDKmsRequest.setIv(ivBytes);
        decryptDKmsRequest.setCiphertextBlob(ciphertextBytes);
        String encryptionContext = query.get("EncryptionContext");
        if (!StringUtils.isEmpty(encryptionContext)) {
            decryptDKmsRequest.setAad(EncryptionContextUtils.sortAndEncode(encryptionContext, runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()));
        }
        return decryptDKmsRequest;
    }

    @Override
    public DecryptResponse callDKMS(DecryptRequest dkmsRequest, KmsRuntimeOptions runtimeOptions) throws Exception {
        RuntimeOptions dkmsRuntimeOptions = transferRuntimeOptions(runtimeOptions);
        dkmsRuntimeOptions.setResponseHeaders(responseHeaders);
        return client.decryptWithOptions(dkmsRequest, dkmsRuntimeOptions);
    }

    @Override
    public Map<String, ?> transferToOpenApiResponse(DecryptResponse response, KmsRuntimeOptions runtimeOptions) throws TeaException {
        DecryptResponseBody body = new DecryptResponseBody();
        Map<String, String> responseHeaders = response.getResponseHeaders();
        String keyVersionId = null;
        if (responseHeaders != null) {
            keyVersionId = responseHeaders.get(Constants.MIGRATION_KEY_VERSION_ID_KEY);
        }
        final com.aliyun.kms20160120.models.DecryptResponse decryptKmsResponse = new com.aliyun.kms20160120.models.DecryptResponse();
        body.setKeyId(response.getKeyId());
        body.setKeyVersionId(keyVersionId);
        body.setPlaintext(new String(response.getPlaintext(), runtimeOptions.getCharset() == null ? this.kmsConfig.getCharset() : runtimeOptions.getCharset()));
        body.setRequestId(response.getRequestId());
        decryptKmsResponse.setStatusCode(HttpURLConnection.HTTP_OK);
        decryptKmsResponse.setHeaders(responseHeaders);
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
