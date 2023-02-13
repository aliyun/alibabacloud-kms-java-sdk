package com.aliyun.kms.kms20160120;

import com.aliyun.kms.kms20160120.handlers.*;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.ApiNames;
import com.aliyun.kms20160120.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teaopenapi.models.OpenApiRequest;
import com.aliyun.teaopenapi.models.Params;
import com.aliyun.teautil.models.RuntimeOptions;

import java.util.HashMap;
import java.util.Map;

public class Client extends com.aliyun.kms20160120.Client {
    private Map<String, KmsTransferHandler> handlerMap = new HashMap<>();
    private com.aliyun.dkms.gcs.sdk.Client client;
    private KmsConfig kmsConfig;
    /**
     * 针对所有接口使用KMS共享网关开关 true:使用KMS共享网关 false:使用KMS实例网关
     */
    private boolean isUseKmsShareGateway;

    public Client(Config config) throws Exception {
        super(config);
        this.isUseKmsShareGateway = true;
    }

    public Client(com.aliyun.dkms.gcs.openapi.models.Config kmsConfig) throws Exception {
        this(new Config().setEndpoint(kmsConfig.getEndpoint()), kmsConfig, false);
    }

    public Client(Config config, com.aliyun.dkms.gcs.openapi.models.Config kmsConfig) throws Exception {
        this(config, kmsConfig, false);
    }

    public Client(Config config, com.aliyun.dkms.gcs.openapi.models.Config kmsConfig, boolean isUseKmsShareGateway) throws Exception {
        super(config);
        if (kmsConfig instanceof KmsConfig) {
            this.kmsConfig = (KmsConfig) kmsConfig;
        }
        this.isUseKmsShareGateway = isUseKmsShareGateway;
        client = new com.aliyun.dkms.gcs.sdk.Client(kmsConfig);
        initKmsTransferHandler();
    }

    private void initKmsTransferHandler() {
        handlerMap.put(ApiNames.ASYMMETRIC_DECRYPT_API_NAME, new AsymmetricDecryptTransferHandler(client, this.kmsConfig, ApiNames.ASYMMETRIC_DECRYPT_API_NAME));
        handlerMap.put(ApiNames.ASYMMETRIC_ENCRYPT_API_NAME, new AsymmetricEncryptTransferHandler(client, this.kmsConfig, ApiNames.ASYMMETRIC_DECRYPT_API_NAME));
        handlerMap.put(ApiNames.ENCRYPT_API_NAME, new EncryptTransferHandler(client, this.kmsConfig, ApiNames.ENCRYPT_API_NAME));
        handlerMap.put(ApiNames.DECRYPT_API_NAME, new DecryptTransferHandler(client, this.kmsConfig, ApiNames.DECRYPT_API_NAME));
        handlerMap.put(ApiNames.ASYMMETRIC_SIGN_API_NAME, new AsymmetricSignTransferHandler(client, this.kmsConfig, ApiNames.ASYMMETRIC_SIGN_API_NAME));
        handlerMap.put(ApiNames.ASYMMETRIC_VERIFY_API_NAME, new AsymmetricVerifyTransferHandler(client, this.kmsConfig, ApiNames.ASYMMETRIC_VERIFY_API_NAME));
        handlerMap.put(ApiNames.GENERATE_DATA_KEY_API_NAME, new GenerateDataKeyTransferHandler(client, this.kmsConfig, ApiNames.GENERATE_DATA_KEY_API_NAME));
        handlerMap.put(ApiNames.GENERATE_DATA_KEY_WITHOUT_PLAINTEXT_API_NAME, new GenerateDataKeyWithoutPlaintextTransferHandler(client, this.kmsConfig, ApiNames.GENERATE_DATA_KEY_WITHOUT_PLAINTEXT_API_NAME));
        handlerMap.put(ApiNames.GET_PUBLIC_KEY_API_NAME, new GetPublicKeyTransferHandler(client, this.kmsConfig, ApiNames.GET_PUBLIC_KEY_API_NAME));
        handlerMap.put(ApiNames.GET_SECRET_VALUE_API_NAME, new GetSecretValueTransferHandler(client, this.kmsConfig, ApiNames.GET_SECRET_VALUE_API_NAME));
    }

    @Override
    public Map<String, ?> callApi(Params params, OpenApiRequest request, RuntimeOptions runtime) throws Exception {
        String action = params.action;
        KmsRuntimeOptions runtimeOptions;
        if (runtime instanceof KmsRuntimeOptions) {
            runtimeOptions = (KmsRuntimeOptions) runtime;
        } else {
            runtimeOptions = new KmsRuntimeOptions(runtime);
        }
        if (isUseKmsShareGateway(action, runtimeOptions)) {
            return super.callApi(params, request, runtimeOptions);
        }
        if (this.kmsConfig != null && !this.kmsConfig.getIgnoreSSLVerifySwitch()) {
            runtimeOptions.setIgnoreSSL(false);
        }
        if (handlerMap.containsKey(action)) {
            return handlerMap.get(action).calApi(request, runtimeOptions);
        }
        return super.callApi(params, request, runtime);
    }

    private boolean isUseKmsShareGateway(String action, KmsRuntimeOptions runtimeOptions) {
        if (runtimeOptions.getIsUseKmsShareGateway() != null) {
            return runtimeOptions.getIsUseKmsShareGateway();
        }
        if (kmsConfig != null && this.kmsConfig.getDefaultKmsApiNames().contains(action)) {
            return true;
        }
        return this.isUseKmsShareGateway;
    }

    public AsymmetricDecryptResponse asymmetricDecryptWithOptions(AsymmetricDecryptRequest request, KmsRuntimeOptions runtime) throws Exception {
        return super.asymmetricDecryptWithOptions(request, runtime);
    }

    @Override
    public AsymmetricDecryptResponse asymmetricDecrypt(AsymmetricDecryptRequest request) throws Exception {
        return super.asymmetricDecryptWithOptions(request, new KmsRuntimeOptions());
    }

    public AsymmetricEncryptResponse asymmetricEncryptWithOptions(AsymmetricEncryptRequest request, KmsRuntimeOptions runtime) throws Exception {
        return super.asymmetricEncryptWithOptions(request, runtime);
    }

    @Override
    public AsymmetricEncryptResponse asymmetricEncrypt(AsymmetricEncryptRequest request) throws Exception {
        return super.asymmetricEncryptWithOptions(request, new KmsRuntimeOptions());
    }

    public AsymmetricSignResponse asymmetricSignWithOptions(AsymmetricSignRequest request, KmsRuntimeOptions runtime) throws Exception {
        return super.asymmetricSignWithOptions(request, runtime);
    }

    @Override
    public AsymmetricSignResponse asymmetricSign(AsymmetricSignRequest request) throws Exception {
        return super.asymmetricSignWithOptions(request, new KmsRuntimeOptions());
    }

    public AsymmetricVerifyResponse asymmetricVerifyWithOptions(AsymmetricVerifyRequest request, KmsRuntimeOptions runtime) throws Exception {
        return super.asymmetricVerifyWithOptions(request, runtime);
    }

    @Override
    public AsymmetricVerifyResponse asymmetricVerify(AsymmetricVerifyRequest request) throws Exception {
        return super.asymmetricVerifyWithOptions(request, new KmsRuntimeOptions());
    }

    @Override
    public DecryptResponse decrypt(DecryptRequest request) throws Exception {
        return super.decryptWithOptions(request, new KmsRuntimeOptions());
    }


    public DecryptResponse decryptWithOptions(DecryptRequest tmpReq, KmsRuntimeOptions runtime) throws Exception {
        return super.decryptWithOptions(tmpReq, runtime);
    }

    public EncryptResponse encryptWithOptions(EncryptRequest tmpReq, KmsRuntimeOptions runtime) throws Exception {
        return super.encryptWithOptions(tmpReq, runtime);
    }

    @Override
    public EncryptResponse encrypt(EncryptRequest request) throws Exception {
        return super.encryptWithOptions(request, new KmsRuntimeOptions());
    }

    public GenerateDataKeyResponse generateDataKeyWithOptions(GenerateDataKeyRequest tmpReq, KmsRuntimeOptions runtime) throws Exception {
        return super.generateDataKeyWithOptions(tmpReq, runtime);
    }

    @Override
    public GenerateDataKeyResponse generateDataKey(GenerateDataKeyRequest request) throws Exception {
        return super.generateDataKeyWithOptions(request, new KmsRuntimeOptions());
    }

    public GenerateDataKeyWithoutPlaintextResponse generateDataKeyWithoutPlaintextWithOptions(GenerateDataKeyWithoutPlaintextRequest tmpReq, KmsRuntimeOptions runtime) throws Exception {
        return super.generateDataKeyWithoutPlaintextWithOptions(tmpReq, runtime);
    }

    @Override
    public GenerateDataKeyWithoutPlaintextResponse generateDataKeyWithoutPlaintext(GenerateDataKeyWithoutPlaintextRequest request) throws Exception {
        return super.generateDataKeyWithoutPlaintextWithOptions(request, new KmsRuntimeOptions());
    }

    public GetPublicKeyResponse getPublicKeyWithOptions(GetPublicKeyRequest request, KmsRuntimeOptions runtime) throws Exception {
        return super.getPublicKeyWithOptions(request, runtime);
    }

    @Override
    public GetPublicKeyResponse getPublicKey(GetPublicKeyRequest request) throws Exception {
        return super.getPublicKeyWithOptions(request, new KmsRuntimeOptions());
    }

    public GetSecretValueResponse getSecretValueWithOptions(GetSecretValueRequest request, KmsRuntimeOptions runtime) throws Exception {
        return super.getSecretValueWithOptions(request, runtime);
    }

    @Override
    public GetSecretValueResponse getSecretValue(GetSecretValueRequest request) throws Exception {
        return super.getSecretValueWithOptions(request, new KmsRuntimeOptions());
    }
}
