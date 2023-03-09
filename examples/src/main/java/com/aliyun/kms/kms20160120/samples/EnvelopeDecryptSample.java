package com.aliyun.kms.kms20160120.samples;

import com.aliyun.kms.kms20160120.Client;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms20160120.models.DecryptRequest;
import com.aliyun.kms20160120.models.DecryptResponse;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EnvelopeDecryptSample {
    private static Base64 base64 = new Base64();

    private static final int GCM_TAG_LENGTH = 16;

    public static void main(String[] args) throws Exception {
        newUserEnvelopeDecrypt();
        beforeMigrateEnvelopeDecrypt();
        afterMigrateEnvelopeDecrypt();
    }

    /**
     * 新接入用户可以参考此方法调用KMS实例网关的服务。
     */
    private static void newUserEnvelopeDecrypt() throws Exception {
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
                .setEndpoint("your-dkms-endpoint")
                // 如需验证服务端证书，这里需要设置为您的服务端证书路径
                .setCaFilePath("path/to/yourCaCert");
        // 或者，设置为您的服务端证书内容
        //.setCa("your-ca-certificate-content"));
        Client client = new Client(config);
        envelopeDecrypt(client);
    }

    /**
     * 密钥迁移前示例代码
     *
     * @throws Exception
     */
    private static void beforeMigrateEnvelopeDecrypt() throws Exception {
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
        envelopeDecrypt(client);
    }

    /**
     * 密钥迁移后示例代码
     *
     * @throws Exception
     */
    private static void afterMigrateEnvelopeDecrypt() throws Exception {
        //创建kms config并设置相应参数
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()                //设置KMS共享网关的域名
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
                .setEndpoint("your-dkms-endpoint")
                // 如需验证服务端证书，这里需要设置为您的服务端证书路径
                .setCaFilePath("path/to/yourCaCert");
        // 或者，设置为您的服务端证书内容
        //.setCa("your-ca-certificate-content"));
        //创建kms client
        Client client = new Client(config, kmsConfig);
        envelopeDecrypt(client);
    }

    /**
     * 信封解密
     *
     * @param client
     * @throws Exception
     */
    private static void envelopeDecrypt(Client client) throws Exception {

        EnvelopeEncryptSample.EnvelopeCipherPersistObject envelopeCipherPersistObject = getEnvelopeCipherPersistObject();
        String encryptedDataKey = envelopeCipherPersistObject.getEncryptedDataKey();
        byte[] iv = base64.decode(envelopeCipherPersistObject.getIv());
        byte[] cipherText = base64.decode(envelopeCipherPersistObject.getCipherText());

        try {
            DecryptRequest decryptRequest = new DecryptRequest();
            decryptRequest.setCiphertextBlob(encryptedDataKey);
            KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //如需忽略SSL证书认证,可打开如下代码并设置ignoreSSLVerifySwitch为true
            //runtimeOptions.ignoreSSL = true;
            DecryptResponse decryptResponse = client.decryptWithOptions(decryptRequest, runtimeOptions);
            byte[] plaintext = base64.decode(decryptResponse.getBody().getPlaintext());

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(plaintext, "AES");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
            byte[] decryptedData = cipher.doFinal(cipherText);
            System.out.println(new String(decryptedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static EnvelopeEncryptSample.EnvelopeCipherPersistObject getEnvelopeCipherPersistObject() {
        // TODO 用户需要在此处代码进行替换，从存储中读取封信加密持久化对象
        return new EnvelopeEncryptSample.EnvelopeCipherPersistObject();
    }

}