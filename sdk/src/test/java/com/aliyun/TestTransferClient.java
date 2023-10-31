package com.aliyun;

import com.aliyun.dkms.gcs.openapi.models.Config;
import com.aliyun.kms.kms20160120.TransferClient;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.ApiNames;
import com.aliyun.kms20160120.Client;
import com.aliyun.kms20160120.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.utils.ConfigUtils;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;


public class TestTransferClient {

    Properties properties;
    com.aliyun.kms20160120.Client client;

    @Before
    public void init() throws Exception {
        properties = ConfigUtils.loadParam("");
        Config dkmsConfig = new KmsConfig()
//                        .setDefaultKmsApiNames(new ArrayList<String>() {{add(Constants.GET_SECRET_VALUE_API_NAME);}})
                .setIgnoreSSLVerifySwitch(true)
                .setProtocol(properties.getProperty("config.protocol"))
                .setClientKeyContent(properties.getProperty("config.clientKeyContent"))
                .setPassword(properties.getProperty("config.password"))
                .setEndpoint(properties.getProperty("config.endpoint"))
                ;
        String accessKeyId = System.getenv("accessKeyId");
        String accessKeySecret = System.getenv("accessKeySecret");
        client = new TransferClient(
                new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint(properties.getProperty("kms.endpoint"))
                ,
                dkmsConfig
//                ,true
        );
    }
    @Test
    public void testAsymmetricEncrypt() throws Exception {
        asymmetricEncrypt();
    }

    @Test
    public void testAsymmetricDecrypt() throws Exception {
        AsymmetricDecryptRequest request = new AsymmetricDecryptRequest();
        request.setCiphertextBlob(asymmetricEncrypt().getBody().ciphertextBlob);
//        request.setCiphertextBlob(properties.getProperty("asymmetric.decrypt.ciphertextBlob"));
        request.setKeyId(properties.getProperty("asymmetric.encrypt.keyId"));
        request.setAlgorithm(properties.getProperty("asymmetric.encrypt.algorithm"));
        request.setKeyVersionId(properties.getProperty("asymmetric.encrypt.key.versionId"));
        try {
            AsymmetricDecryptResponse response = client.asymmetricDecryptWithOptions(request,
                    new KmsRuntimeOptions().setCharset(charset).setIgnoreSSL(true)
//                            .setIsUseKmsShareGateway(true)
            );;
            System.out.printf("KeyId: %s%n", response.getBody().keyId);
            System.out.printf("KeyVersionId: %s%n", response.getBody().keyVersionId);
            System.out.printf("Plaintext: %s%n", response.getBody().plaintext);
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAsymmetricSign() throws Exception {
        asymmetricSign();
    }

    @Test
    public void testAsymmetricVerify() throws Exception {
        AsymmetricVerifyRequest request = new AsymmetricVerifyRequest();
        request.setKeyId(properties.getProperty("asymmetric.sign.keyId"));
        request.setAlgorithm(properties.getProperty("asymmetric.sign.algorithm"));
        request.setDigest(properties.getProperty("asymmetric.sign.digest"));
        request.setKeyVersionId(properties.getProperty("asymmetric.sign.key.versionId"));
        request.setValue(asymmetricSign().getBody().value);
        try {
            AsymmetricVerifyResponse response = client.asymmetricVerifyWithOptions(request,
                    new KmsRuntimeOptions().setCharset(charset).setIgnoreSSL(true)
//                            .setIsUseKmsShareGateway(true)
            );;
            System.out.printf("KeyId: %s%n", response.getBody().keyId);
            System.out.printf("KeyVersionId: %s%n", response.getBody().keyVersionId);
            System.out.printf("value: %s%n", response.getBody().value);
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public DecryptResponse decrypt(String ciphertextBlob) throws Exception {
        try {
            DecryptRequest request = new DecryptRequest();
            request.setCiphertextBlob(ciphertextBlob);
            String context = properties.getProperty("encrypt.encryption.context");
            if (context != null) {
                request.setEncryptionContext(new HashMap<String, String>() {{
                    put("context", context);
                    put("context1", "123");
                }});
            }
            DecryptResponse response = client.decryptWithOptions(request, new KmsRuntimeOptions()
                            .setCharset(charset).setIgnoreSSL(true)
//                            .setIsUseKmsShareGateway(true)
            );
            System.out.printf("KeyId: %s%n", response.getBody().keyId);
            System.out.printf("KeyVersionId: %s%n", response.getBody().keyVersionId);
            System.out.printf("Plaintext: %s%n", response.getBody().plaintext);
            System.out.printf("Plaintext bytes: %s%n", Arrays.toString(response.body.plaintext.getBytes(charset)));
            return response;
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testEncrypt() throws Exception {
        encrypt();
    }

    private static final Base64 base64 = new Base64();

    @Test
    public void testDecrypt() throws Exception {
        String ciphertextBlob = encrypt().getBody().ciphertextBlob;
        decrypt(ciphertextBlob);
    }

    @Test
    public void testGenerateDataKey() throws Exception {
        GenerateDataKeyRequest request = new GenerateDataKeyRequest();
        request.setKeyId(properties.getProperty("encrypt.keyId"));
        String context = properties.getProperty("encrypt.encryption.context");
        if (context != null) {
            request.setEncryptionContext(new HashMap<String, String>() {{
                put("context1", "123");
                put("context", context);
            }});
        }
        try {
            GenerateDataKeyResponse response = client.generateDataKeyWithOptions(request,
                    new KmsRuntimeOptions().setCharset(charset).setIgnoreSSL(true)
//                            .setIsUseKmsShareGateway(true)
            );;
            System.out.printf("KeyId: %s%n", response.getBody().keyId);
            System.out.printf("KeyVersionId: %s%n", response.getBody().keyVersionId);
            System.out.printf("CiphertextBlob: %s%n", response.getBody().ciphertextBlob);
            System.out.printf("Plaintext: %s%n", response.getBody().plaintext);
            DecryptResponse decryptResponse = decrypt(response.getBody().ciphertextBlob);
            assert response.getBody().plaintext.equals(decryptResponse.getBody().plaintext);
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGenerateDataKeyWithoutPlaintext() throws Exception {
        GenerateDataKeyWithoutPlaintextRequest request = new GenerateDataKeyWithoutPlaintextRequest();
        request.setKeyId(properties.getProperty("encrypt.keyId"));
        String context = properties.getProperty("encrypt.encryption.context");
        if (context != null) {
            request.setEncryptionContext(new HashMap<String, String>() {{
                put("context", context);
                put("context1", "123");
            }});
        }
        try {
            GenerateDataKeyWithoutPlaintextResponse response = client.generateDataKeyWithoutPlaintextWithOptions(request,
                    new KmsRuntimeOptions().setCharset(charset).setIgnoreSSL(true)
//                            .setIsUseKmsShareGateway(true)
            );;
            System.out.printf("KeyId: %s%n", response.getBody().keyId);
            System.out.printf("KeyVersionId: %s%n", response.getBody().keyVersionId);
            System.out.printf("CiphertextBlob: %s%n", response.getBody().ciphertextBlob);
            decrypt(response.getBody().ciphertextBlob);
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetPublicKey() throws Exception {
        GetPublicKeyRequest request = new GetPublicKeyRequest();
        request.setKeyId(properties.getProperty("asymmetric.encrypt.keyId"));
        request.setKeyVersionId(properties.getProperty("asymmetric.encrypt.key.versionId"));
        try {
            GetPublicKeyResponse response = client.getPublicKeyWithOptions(request,
                    new KmsRuntimeOptions().setIgnoreSSL(true)
//                            .setIsUseKmsShareGateway(true)
            );
            System.out.printf("KeyId: %s%n", response.getBody().keyId);
            System.out.printf("KeyVersionId: %s%n", response.getBody().keyVersionId);
            System.out.printf("PublicKey: %s%n", response.getBody().publicKey);
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

//    @Test
    public void getSecretValue() throws Exception {
        com.aliyun.dkms.gcs.openapi.models.Config config = new com.aliyun.dkms.gcs.openapi.models.Config()
                .setProtocol(properties.getProperty("config.protocol"))
                .setClientKeyContent(properties.getProperty("config.clientKeyContent"))
                .setPassword(properties.getProperty("config.password"))
                .setEndpoint(properties.getProperty("config.endpoint"));
        Client client = new TransferClient(config);
        GetSecretValueRequest request = new GetSecretValueRequest();
        request.setSecretName(properties.getProperty("secret.name"));
        try {
            GetSecretValueResponse response = client.getSecretValueWithOptions(request,
                    new KmsRuntimeOptions().setCharset(charset).setIgnoreSSL(true));
            System.out.printf("SecretData: %s%n", response.getBody().getSecretData());
            System.out.printf("ExtendedConfig: %s%n", response.getBody().extendedConfig);
            System.out.printf("CreateTime: %s%n", response.getBody().getCreateTime());
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetSecretValue() throws Exception {
        GetSecretValueRequest request = new GetSecretValueRequest();
        request.setSecretName(properties.getProperty("secret.name"));
        try {
            GetSecretValueResponse response = client.getSecretValueWithOptions(request,
                    new KmsRuntimeOptions().setCharset(charset).setIgnoreSSL(true)
//                            .setIsUseKmsShareGateway(true)
            );;
            System.out.println(new Gson().toJson(response));
            System.out.printf("SecretData: %s%n", response.getBody().getSecretData());
            System.out.printf("ExtendedConfig: %s%n", response.getBody().extendedConfig);
            System.out.printf("CreateTime: %s%n", response.getBody().getCreateTime());
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    Charset charset = StandardCharsets.UTF_8;

    public EncryptResponse encrypt() throws Exception {
        EncryptRequest request = new EncryptRequest();
        request.setKeyId(properties.getProperty("encrypt.keyId"));
        request.setPlaintext(properties.getProperty("encrypt.plaintext"));
        String context = properties.getProperty("encrypt.encryption.context");
        if (context != null) {
            request.setEncryptionContext(new HashMap<String, String>() {{
                put("context1", "123");
                put("context", context);
            }});
        }
        try {
            EncryptResponse response = client.encryptWithOptions(request, new KmsRuntimeOptions()
                            .setCharset(charset)
                            //.setIgnoreSSL(true)
//                            .setIsUseKmsShareGateway(true)
            );
            System.out.printf("KeyId: %s%n", response.getBody().keyId);
            System.out.printf("KeyVersionId: %s%n", response.getBody().keyVersionId);
            System.out.printf("CiphertextBlob: %s%n", response.getBody().ciphertextBlob);
            return response;
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public AsymmetricSignResponse asymmetricSign() throws Exception {
        AsymmetricSignRequest request = new AsymmetricSignRequest();
        request.setKeyId(properties.getProperty("asymmetric.sign.keyId"));
        request.setAlgorithm(properties.getProperty("asymmetric.sign.algorithm"));
        request.setDigest(properties.getProperty("asymmetric.sign.digest"));
        request.setKeyVersionId(properties.getProperty("asymmetric.sign.key.versionId"));
        try {
            AsymmetricSignResponse response = client.asymmetricSignWithOptions(request,
                    new KmsRuntimeOptions().setCharset(charset).setIgnoreSSL(true)
//                            .setIsUseKmsShareGateway(true)
            );;
            System.out.printf("KeyId: %s%n", response.getBody().keyId);
            System.out.printf("KeyVersionId: %s%n", response.getBody().keyVersionId);
            System.out.printf("Value: %s%n", response.getBody().value);
            return response;
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private AsymmetricEncryptResponse asymmetricEncrypt() throws Exception {
        AsymmetricEncryptRequest request = new AsymmetricEncryptRequest();
        request.setPlaintext(properties.getProperty("asymmetric.encrypt.plaintext"));
        request.setKeyId(properties.getProperty("asymmetric.encrypt.keyId"));
        request.setKeyVersionId(properties.getProperty("asymmetric.encrypt.keyVersionId"));
        request.setAlgorithm(properties.getProperty("asymmetric.encrypt.algorithm"));
        request.setKeyVersionId(properties.getProperty("asymmetric.encrypt.key.versionId"));
        try {
            AsymmetricEncryptResponse response = client.asymmetricEncryptWithOptions(request,
                    new KmsRuntimeOptions().setCharset(charset).setIgnoreSSL(true)
//                            .setIsUseKmsShareGateway(true)
            );
            System.out.printf("CiphertextBlob: %s%n", response.getBody().ciphertextBlob);
            System.out.printf("KeyId: %s%n", response.getBody().keyId);
            System.out.printf("KeyVersionId: %s%n", response.getBody().keyVersionId);
            return response;
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}