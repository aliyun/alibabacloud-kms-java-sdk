package com.aliyun.kms.kms20160120;

import com.aliyun.kms.kms20160120.Client;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms20160120.models.GetSecretValueRequest;
import com.aliyun.kms20160120.models.GetSecretValueResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;


public class GetSecretValueSample {

    public static void main(String[] args) throws Exception {
        newUserGetSecretValue();
        beforeMigrateGetSecretValue();
        afterMigrateGetSecretValue();
    }

    /**
     * 新接入用户可以参考此方法调用KMS实例网关的服务。
     */
    private static void newUserGetSecretValue() throws Exception {
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
        getSecretValue(client);
    }

    /**
     * 密钥迁移前示例代码
     *
     * @throws Exception
     */
    private static void beforeMigrateGetSecretValue() throws Exception {
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
        getSecretValue(client);
    }

    /**
     * 密钥迁移后示例代码
     *
     * @throws Exception
     */
    private static void afterMigrateGetSecretValue() throws Exception {
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
        getSecretValue(client);
    }

    public static void getSecretValue(Client client) {

        try {
            //创建GetSecretValue请求体
            GetSecretValueRequest request = new GetSecretValueRequest();
            //设置SecretName参数
            request.setSecretName("your-secret-name");
            //创建kms 运行时配置参数
            RuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //如需忽略SSL证书认证,可打开如下代码并设置ignoreSSLVerifySwitch为true
            //runtimeOptions.ignoreSSL = true;
            GetSecretValueResponse response = client.getSecretValueWithOptions(request, runtimeOptions);
            System.out.printf("SecretData: %s%n", response.getBody().getSecretData());
            System.out.printf("ExtendedConfig: %s%n", response.getBody().getExtendedConfig());
            System.out.printf("CreateTime: %s%n", response.getBody().getCreateTime());
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
