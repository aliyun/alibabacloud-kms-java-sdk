![](https://aliyunsdk-pages.alicdn.com/icons/AlibabaCloud.svg)

阿里云KMS Java SDK
=====================

阿里云KMS Java SDK可以帮助Java开发者快速使用KMS。

*其他语言版本:*[English](README.md)*,*[简体中文](README.zh-cn.md)

-   [阿里云KMS主页](https://help.aliyun.com/document_detail/311016.html)
-   [代码示例](/examples)
-   [Issues](https://github.com/aliyun/alibabacloud-kms-java-sdk/issues)
-   [Release](https://github.com/aliyun/alibabacloud-kms-java-sdk/releases)

许可证
------ 

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

优势
------ 
帮助Java开发者通过本SDK快速使用阿里云KMS产品的所有API：
- 支持通过KMS公共网关访问进行KMS资源管理和密钥运算
- 支持通过KMS实例网关进行密钥运算

软件要求
----------

- Java 1.8 或以上版本
- Maven

安装
----------

可以通过Maven的方式在项目中使用KMS Java客户端。导入方式如下:

```
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>alibabacloud-kms-java-sdk</artifactId>
    <version>1.2.5</version>
</dependency>
```

构建
----------

你可以从Github检出代码通过下面的maven命令进行构建。

```
mvn clean install -DskipTests -Dgpg.skip=true
```
KMS Client介绍
----------

| KMS 客户端Java类 | 简介 | 使用场景 |
| :-----| :---- | :---- |
| com.aliyun.kms.kms20160120.Client | 支持KMS资源管理和KMS实例网关的密钥运算| 1.仅通过VPC网关进行密钥运算操作的场景。<br>2.仅通过公共网关对KMS资源管理的场景。 <br>3.既要通过VPC网关进行密钥运算操作又要通过公共网关对KMS资源管理的场景。|
| com.aliyun.kms.kms20160120.TransferClient | 支持用户应用简单修改的情况下就可以从KMS 1.0密钥运算迁移到 KMS 3.0密钥运算 | 使用阿里云 SDK访问KMS 1.0密钥运算的用户，需要迁移到KMS 3.0的场景。|

示例代码
----------
### 1. 仅通过VPC网关进行密钥运算操作的场景。
#### 参考以下示例代码调用KMS Encrypt API。更多API示例参考 [密钥运算示例代码](./examples/src/main/java/com/aliyun/kms/kms20160120/samples/operation)
```Java
public class Encrypt {

    public static com.aliyun.dkms.gcs.openapi.models.Config createKmsInstanceConfig(String clientKeyFile, String password, String endpoint, String caFilePath) throws Exception {
        com.aliyun.dkms.gcs.openapi.models.Config config = new com.aliyun.dkms.gcs.openapi.models.Config();
        config.clientKeyFile = clientKeyFile;
        config.password = password;
        config.endpoint = endpoint;
        config.caFilePath = caFilePath;
        return config;
    }

    public static com.aliyun.kms.kms20160120.Client createClient(com.aliyun.dkms.gcs.openapi.models.Config kmsInstanceConfig) throws Exception {
        return new com.aliyun.kms.kms20160120.Client(kmsInstanceConfig);
    }

    public static com.aliyun.dkms.gcs.sdk.models.EncryptResponse encrypt(com.aliyun.kms.kms20160120.Client client, String keyId, byte[] plaintext, String algorithm, byte[] aad) throws Exception {
        com.aliyun.dkms.gcs.sdk.models.EncryptRequest request = new com.aliyun.dkms.gcs.sdk.models.EncryptRequest()
                .setKeyId(keyId)
                .setPlaintext(plaintext)
                .setAad(aad)
                .setAlgorithm(algorithm);
        return client.encrypt(request);
    }

    public static void main(String[] args_) throws Exception {
        com.aliyun.dkms.gcs.openapi.models.Config kmsInstanceConfig = Encrypt.createKmsInstanceConfig(com.aliyun.darabonba.env.EnvClient.getEnv("your client key file path env"), com.aliyun.darabonba.env.EnvClient.getEnv("your client key password env"), "your kms instance endpoint env", "your ca file path");
        com.aliyun.kms.kms20160120.Client client = Encrypt.createClient(kmsInstanceConfig);

        byte[] aad = com.aliyun.teautil.Common.toBytes("your aad");
        String keyId = "your keyId";
        byte[] plaintext = com.aliyun.teautil.Common.toBytes("your plaintext");
        String algorithm = "your algorithm";
        com.aliyun.dkms.gcs.sdk.models.EncryptResponse response = Encrypt.encrypt(client, keyId, plaintext, algorithm, aad);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(response));
    }
}
```
### 2. 仅通过公共网关对KMS资源管理的场景。
#### 参考以下示例代码调用KMS CreateKey API。更多API示例参考 [密钥管理代码示例](./examples/src/main/java/com/aliyun/kms/kms20160120/samples/manage)
```Java
public class CreateKey {

    //创建OpenApi配置
    public static com.aliyun.teaopenapi.models.Config createOpenApiConfig(String accessKeyId, String accessKeySecret, String regionId) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.accessKeyId = accessKeyId;
        config.accessKeySecret = accessKeySecret;
        config.regionId = regionId;
        return config;
    }

    //创建Client 
    public static com.aliyun.kms.kms20160120.Client createClient(com.aliyun.teaopenapi.models.Config openApiConfig) throws Exception {
        return new com.aliyun.kms.kms20160120.Client(openApiConfig);
    }
    //创建密钥
    public static com.aliyun.kms20160120.models.CreateKeyResponse createKey(com.aliyun.kms.kms20160120.Client client, Boolean enableAutomaticRotation, String rotationInterval, String keyUsage, String origin, String description, String DKMSInstanceId, String protectionLevel, String keySpec) throws Exception {
        com.aliyun.kms20160120.models.CreateKeyRequest request = new com.aliyun.kms20160120.models.CreateKeyRequest()
                .setEnableAutomaticRotation(enableAutomaticRotation)
                .setRotationInterval(rotationInterval)
                .setKeyUsage(keyUsage)
                .setOrigin(origin)
                .setDescription(description)
                .setDKMSInstanceId(DKMSInstanceId)
                .setProtectionLevel(protectionLevel)
                .setKeySpec(keySpec);
        return client.createKey(request);
    }

    public static void main(String[] args_) throws Exception {
        // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        com.aliyun.teaopenapi.models.Config openApiConfig = CreateKey.createOpenApiConfig(com.aliyun.darabonba.env.EnvClient.getEnv("ALIBABA_CLOUD_ACCESS_KEY_ID"), com.aliyun.darabonba.env.EnvClient.getEnv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"), "your regionId");
        com.aliyun.kms.kms20160120.Client client = CreateKey.createClient(openApiConfig);    
        Boolean enableAutomaticRotation = false;
        String rotationInterval = "your rotationInterval";
        String keyUsage = "your keyUsage";
        String origin = "your origin";
        String description = "your description";
        String dKMSInstanceId = "your dKMSInstanceId";
        String protectionLevel = "your protectionLevel";
        String keySpec = "your keySpec";
        com.aliyun.kms20160120.models.CreateKeyResponse response = CreateKey.createKey(client, enableAutomaticRotation, rotationInterval, keyUsage, origin, description, dKMSInstanceId, protectionLevel, keySpec);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(response));
    }
}
```
### 3. 既要通过VPC网关进行密钥运算操作又要通过公共网关对KMS资源管理的场景。
#### 参考以下示例代码调用KMS CreateKey API 和 Encrypt API。更多API示例参考 [密钥运算示例代码](./examples/src/main/java/com/aliyun/kms/kms20160120/samples/operation) 和 [密钥管理示例代码](./examples/src/main/java/com/aliyun/kms/kms20160120/samples/manage)
```Java

public class Sample {

    //创建kms实例配置
    public static com.aliyun.dkms.gcs.openapi.models.Config createKmsInstanceConfig(String clientKeyFile, String password, String endpoint, String caFilePath) throws Exception {
        com.aliyun.dkms.gcs.openapi.models.Config config = new com.aliyun.dkms.gcs.openapi.models.Config();
        config.clientKeyFile = clientKeyFile;
        config.password = password;
        config.endpoint = endpoint;
        config.caFilePath = caFilePath;
        return config;
    }

    //创建OpenApi配置
    public static com.aliyun.teaopenapi.models.Config createOpenApiConfig(String accessKeyId, String accessKeySecret, String regionId) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.accessKeyId = accessKeyId;
        config.accessKeySecret = accessKeySecret;
        config.regionId = regionId;
        return config;
    }

    //创建Client 
    public static com.aliyun.kms.kms20160120.Client createClient(com.aliyun.dkms.gcs.openapi.models.Config kmsInstanceConfig, com.aliyun.teaopenapi.models.Config openApiConfig) throws Exception {
        return new com.aliyun.kms.kms20160120.Client(kmsInstanceConfig, openApiConfig);
    }

    public static com.aliyun.kms20160120.models.CreateKeyResponse createKey(com.aliyun.kms.kms20160120.Client client, Boolean enableAutomaticRotation, String rotationInterval, String keyUsage, String origin, String description, String DKMSInstanceId, String protectionLevel, String keySpec) throws Exception {
        com.aliyun.kms20160120.models.CreateKeyRequest request = new com.aliyun.kms20160120.models.CreateKeyRequest()
                .setEnableAutomaticRotation(enableAutomaticRotation)
                .setRotationInterval(rotationInterval)
                .setKeyUsage(keyUsage)
                .setOrigin(origin)
                .setDescription(description)
                .setDKMSInstanceId(DKMSInstanceId)
                .setProtectionLevel(protectionLevel)
                .setKeySpec(keySpec);
        return client.createKey(request);
    }

    public static com.aliyun.dkms.gcs.sdk.models.EncryptResponse encrypt(com.aliyun.kms.kms20160120.Client client, String keyId, byte[] plaintext, String algorithm, byte[] aad) throws Exception {
        com.aliyun.dkms.gcs.sdk.models.EncryptRequest request = new com.aliyun.dkms.gcs.sdk.models.EncryptRequest()
                .setKeyId(keyId)
                .setPlaintext(plaintext)
                .setAad(aad)
                .setAlgorithm(algorithm);
        return client.encrypt(request);
    }

    public static void main(String[] args_) throws Exception {
        com.aliyun.dkms.gcs.openapi.models.Config kmsInstanceConfig = Sample.createKmsInstanceConfig(com.aliyun.darabonba.env.EnvClient.getEnv("your client key file path env"), com.aliyun.darabonba.env.EnvClient.getEnv("your client key password env"), "your kms instance endpoint env", "your ca file path");
        // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        com.aliyun.teaopenapi.models.Config openApiConfig = Sample.createOpenApiConfig(com.aliyun.darabonba.env.EnvClient.getEnv("ALIBABA_CLOUD_ACCESS_KEY_ID"), com.aliyun.darabonba.env.EnvClient.getEnv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"), "your region id");
        com.aliyun.kms.kms20160120.Client client = Sample.createClient(kmsInstanceConfig, openApiConfig);
        Boolean enableAutomaticRotation = false;
        String rotationInterval = "your rotationInterval";
        String keyUsage = "your keyUsage";
        String origin = "your origin";
        String description = "your description";
        String dKMSInstanceId = "your dKMSInstanceId";
        String protectionLevel = "your protectionLevel";
        String keySpec = "your keySpec";
        com.aliyun.kms20160120.models.CreateKeyResponse createKeyResponse = Sample.createKey(client, enableAutomaticRotation, rotationInterval, keyUsage, origin, description, dKMSInstanceId, protectionLevel, keySpec);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(createKeyResponse));

        byte[] aad = com.aliyun.teautil.Common.toBytes("your aad");
        String keyId = "your keyId";
        byte[] plaintext = com.aliyun.teautil.Common.toBytes("your plaintext");
        String algorithm = "your algorithm";
        com.aliyun.dkms.gcs.sdk.models.EncryptResponse encryptResponse = Sample.encrypt(client, keyId, plaintext, algorithm, aad);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(encryptResponse));
    }
}

```
### 使用阿里云 SDK访问KMS 1.0密钥运算的用户，需要迁移到KMS 3.0的场景。
#### 参考以下示例代码调用KMS API。更多API示例参考 [KMS迁移代码示例](./examples/src/main/java/com/aliyun/kms/kms20160120/samples/transfer)
```Java
import com.aliyun.kms20160120.Client;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms20160120.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;

public class Sample {
    public static void main(String[] args) throws Exception {
        Client client = createClient();
        createKey(client);
        generateDataKey(client);
    }

    private static Client createClient() {
        try {
            //创建调用KMS共享网关的config并设置相应参数
            Config config = new Config()
                    //设置KMS共享网关的域名
                    .setEndpoint("your-kms-endpoint")
                    //设置访问凭证AccessKeyId
                    .setAccessKeyId(System.getenv("your-ak-env-name"))
                    //设置访问凭证AccessKeySecret
                    .setAccessKeySecret(System.getenv("your-sk-env-name"));

            //创建KMS实例网关的config并设置相应参数
            com.aliyun.dkms.gcs.openapi.models.Config kmsConfig
                    = new KmsConfig()
                    //设置请求协议为https
                    .setProtocol("https")
                    //设置KMS实例的Endpoint
                    .setEndpoint("your-dkms-endpoint")
                    //设置client key文件地址
                    .setClientKeyFile("your-client-key-file")
                    //设置client-key密码
                    .setPassword("your-client-key-password")
                    // 设置KMS实例的CA证书。通过提供文件路径
                    .setCaFilePath("path/to/yourCaCert");
            // 或者，CA证书内容。
            //.setCa("your-ca-certificate-content");
            //创建kms client
            return new com.aliyun.kms.kms20160120.TransferClient(config, kmsConfig);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建密钥调用KMS共享网关
     *
     * @param client
     */
    private static void createKey(Client client) {

        try {
            CreateKeyRequest request = new CreateKeyRequest();
            request.setDKMSInstanceId("you-dkms-instancesId");
            CreateKeyResponse response = client.createKey(request);
            System.out.printf("RequestId: %s%n", response.getBody().getRequestId());
            System.out.printf("KeyMetadata: %s%n", response.getBody().getKeyMetadata());
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成数据密钥调用KMS实例网关
     */
    private static void generateDataKey(Client client) {

        try {
            GenerateDataKeyRequest request = new GenerateDataKeyRequest();
            request.setKeyId("your-key-id");
            GenerateDataKeyResponse response = client.generateDataKey(request);
            System.out.printf("KeyId: %s%n", response.getBody().getKeyId());
            System.out.printf("KeyVersionId: %s%n", response.getBody().getKeyVersionId());
            System.out.printf("CiphertextBlob: %s%n", response.getBody().getCiphertextBlob());
            System.out.printf("Plaintext: %s%n", response.getBody().getPlaintext());
            System.out.printf("RequestId: %s%n", response.getBody().getRequestId());
        } catch (TeaException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

版权所有 2009-present, 阿里巴巴集团.
