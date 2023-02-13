package com.aliyun;

import com.aliyun.kms.kms20160120.Client;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms20160120.models.AsymmetricSignRequest;
import com.aliyun.kms20160120.models.AsymmetricSignResponse;
import com.aliyun.tea.TeaException;


public class AsymmetricSignSample {

    public static void main(String[] args) throws Exception {
        newUserAsymmetricSign();
        beforeMigrateAsymmetricSign();
        afterMigrateAsymmetricSign();
    }

    /**
     * 新接入用户可以参考此方法调用KMS实例网关的服务。
     */
    private static void newUserAsymmetricSign() throws Exception {
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
        asymmetricSign(client);
    }

    /**
     * 密钥迁移前示例代码
     *
     * @throws Exception
     */
    private static void beforeMigrateAsymmetricSign() throws Exception {
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
        asymmetricSign(client);
    }

    /**
     * 密钥迁移后示例代码
     *
     * @throws Exception
     */
    private static void afterMigrateAsymmetricSign() throws Exception {
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
                .setEndpoint("your-dkms-endpoint");
        // 如需验证服务端证书，这里需要设置为您的服务端证书路径
        //.setCaFilePath("path/to/yourCaCert")
        // 或者，设置为您的服务端证书内容
        //.setCa("your-ca-certificate-content"));
        //创建kms client
        Client client = new Client(config, kmsConfig);
        asymmetricSign(client);
    }

    /**
     * 非对称签名
     */
    public static void asymmetricSign(Client client) {

        try {
            AsymmetricSignRequest request = new AsymmetricSignRequest();
            request.setKeyId("your-key-id");
            request.setAlgorithm("your-algorithm");
            request.setDigest("your-digest");
            request.setKeyVersionId("your-key-version-id");
            KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //如需忽略SSL证书认证,可打开如下代码并设置ignoreSSLVerifySwitch为true
            //runtimeOptions.ignoreSSL = true;
            AsymmetricSignResponse response = client.asymmetricSignWithOptions(request, runtimeOptions);
            System.out.printf("KeyId: %s%n", response.getBody().getKeyId());
            System.out.printf("KeyVersionId: %s%n", response.getBody().getKeyVersionId());
            System.out.printf("Value: %s%n", response.getBody().getValue());
            System.out.printf("RequestId: %s%n", response.getBody().getRequestId());
        } catch (TeaException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}