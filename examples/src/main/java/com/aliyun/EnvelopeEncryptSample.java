package com.aliyun;

import com.aliyun.kms.kms20160120.Client;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms20160120.models.GenerateDataKeyResponse;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

public class EnvelopeEncryptSample {
    private static Base64 base64 = new Base64();
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;

    public static void main(String[] args) throws Exception {
        newUserEnvelopeEncrypt();
        beforeMigrateEnvelopeEncrypt();
        afterMigrateEnvelopeEncrypt();
    }

    /**
     * 新接入用户可以参考此方法调用KMS实例网关的服务。
     */
    private static void newUserEnvelopeEncrypt() throws Exception {
        //创建kms config并设置相应参数
        com.aliyun.dkms.gcs.openapi.models.Config config
                = new KmsConfig()
                //设置ssl验证标识,默认为false,即需验证ssl证书;为true时,可在调用接口时设置是否忽略ssl证书
                .setIgnoreSSLVerifySwitch(false)
                //设置请求协议为https
                .setProtocol("https")
                //设置client key文件地址
                .setClientKeyFile("your-client-key-file")
                //设置client-key密码
                .setPassword("your-password")
                //设置dkms域名
                .setEndpoint("your-dkms-endpoint");
        // 如需验证服务端证书，这里需要设置为您的服务端证书路径
        //.setCaFilePath("path/to/yourCaCert")
        // 或者，设置为您的服务端证书内容
        //.setCa("your-ca-certificate-content"));
        Client client = new Client(config);
        envelopeEncrypt(client);
    }

    /**
     * 密钥迁移前示例代码
     *
     * @throws Exception
     */
    private static void beforeMigrateEnvelopeEncrypt() throws Exception {
        //创建kms config并设置相应参数
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                //设置KMS共享网关的域名
                .setEndpoint("your-kms-endpoint")
                //设置访问凭证AccessKeyId
                .setAccessKeyId(System.getenv("your-ak-env-name"))
                //设置访问凭证AccessKeySecret
                .setAccessKeySecret(System.getenv("your-sk-env-name"));
        //创建kms client
        Client client = new Client(config);
        envelopeEncrypt(client);
    }

    /**
     * 密钥迁移后示例代码
     *
     * @throws Exception
     */
    private static void afterMigrateEnvelopeEncrypt() throws Exception {
        //创建kms config并设置相应参数
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                 //设置KMS共享网关的域名
                .setEndpoint("your-kms-endpoint")
                //设置访问凭证AccessKeyId
                .setAccessKeyId(System.getenv("your-ak-env-name"))
                //设置访问凭证AccessKeySecret
                .setAccessKeySecret(System.getenv("your-sk-env-name"));
        //创建kms config并设置相应参数
        com.aliyun.dkms.gcs.openapi.models.Config kmsConfig
                = new KmsConfig()
                //设置ssl验证标识,默认为false,即需验证ssl证书;为true时,可在调用接口时设置是否忽略ssl证书
                .setIgnoreSSLVerifySwitch(false)
                //设置请求协议为https
                .setProtocol("https")
                //设置client key文件地址
                .setClientKeyFile("your-client-key-file")
                //设置client-key密码
                .setPassword("your-password")
                //设置dkms域名
                .setEndpoint("your-dkms-endpoint");
        // 如需验证服务端证书，这里需要设置为您的服务端证书路径
        //.setCaFilePath("path/to/yourCaCert")
        // 或者，设置为您的服务端证书内容
        //.setCa("your-ca-certificate-content"));
        //创建kms client
        Client client = new Client(config, kmsConfig);
        envelopeEncrypt(client);
    }

    /**
     * 信封加密
     *
     * @param client
     * @throws Exception
     */
    private static void envelopeEncrypt(Client client) throws Exception {
        com.aliyun.kms20160120.models.GenerateDataKeyRequest request = new com.aliyun.kms20160120.models.GenerateDataKeyRequest();
        request.setKeyId("your-key-id");
        KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();
        //如需忽略SSL证书认证,可打开如下代码并设置ignoreSSLVerifySwitch为true
        //runtimeOptions.ignoreSSL = true;
        GenerateDataKeyResponse response = client.generateDataKeyWithOptions(request, runtimeOptions);
        //KMS返回的数据密钥明文, 加密本地数据使用
        byte[] dataKey = base64.decode(response.body.plaintext);
        String dataKeyBlob = response.body.ciphertextBlob;
        // 使用KMS返回的数据密钥明文在本地对数据进行加密，下面以AES-256 GCM模式为例
        byte[] data = "your plaintext data ".getBytes(StandardCharsets.UTF_8);
        byte[] iv = null; // 加密初始向量，解密时也需要传入
        byte[] cipherText = null; // 密文
        try {
            iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(dataKey, "AES");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
            cipherText = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // 输出密文，密文输出或持久化由用户根据需要进行处理，下面示例仅展示将密文输出到一个对象的情况
        // 假如envelope_cipher_text是需要输出的密文对象，至少需要包括以下三个内容:
        // (1) EncryptedDataKey: KMS返回的数据密钥密文
        // (2) iv: 加密初始向量
        // (3) CipherText: 密文数据
        EnvelopeCipherPersistObject outCipherText = new EnvelopeCipherPersistObject()
                .setEncryptedDataKey(dataKeyBlob)
                .setIv(base64.encode(iv))
                .setCipherText(base64.encode(cipherText));
        System.out.println(outCipherText);
    }

    public static class EnvelopeCipherPersistObject implements Serializable {
        private String encryptedDataKey;
        private byte[] iv;
        private byte[] cipherText;

        public EnvelopeCipherPersistObject setEncryptedDataKey(String encryptedDataKey) {
            this.encryptedDataKey = encryptedDataKey;
            return this;
        }

        public String getEncryptedDataKey() {
            return encryptedDataKey;
        }

        public EnvelopeCipherPersistObject setIv(byte[] iv) {
            this.iv = iv;
            return this;
        }

        public byte[] getIv() {
            return iv;
        }

        public EnvelopeCipherPersistObject setCipherText(byte[] cipherText) {
            this.cipherText = cipherText;
            return this;
        }

        public byte[] getCipherText() {
            return cipherText;
        }

        @Override
        public String toString() {
            return "EnvelopeCipherPersistObject{" +
                    ", encryptedDataKey='" + encryptedDataKey + '\'' +
                    ", iv=" + Arrays.toString(iv) +
                    ", cipherText=" + Arrays.toString(cipherText) +
                    '}';
        }
    }
}