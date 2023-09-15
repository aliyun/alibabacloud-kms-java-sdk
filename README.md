![](https://aliyunsdk-pages.alicdn.com/icons/AlibabaCloud.svg)

Alibaba Cloud KMS SDK for Java
=================================

Alibaba Cloud KMS SDK for Java can help Java developers to use KMS.

*Read this in other
languages:*[English](README.md)*,*[简体中文](README.zh-cn.md)

-   [Alibaba Cloud KMS Homepage](https://www.alibabacloud.com/help/zh/doc-detail/311016.htm)
-   [Sample Code](/example)
-   [Issues](https://github.com/aliyun/alibabacloud-kms-java-sdk/issues)
-   [Release](https://github.com/aliyun/alibabacloud-kms-java-sdk/releases)

License
-------

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

Advantage
------ 
Alibaba Cloud KMS SDK helps Java developers quickly use all APIs of Alibaba Cloud KMS products:
- KMS resource management and key operations can be performed through KMS public gateway access
- You can perform key operations through KMS instance gateway


Requirements
--------

- Java 1.8 or later
- Maven

Install
--------

The recommended way to use the Alibaba Cloud KMS SDK for Java in your project is to consume it from Maven. Import as follows:

```
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>alibabacloud-kms-java-sdk</artifactId>
    <version>1.2.1</version>
</dependency>
```


Build
--------

Once you check out the code from GitHub, you can build it using Maven. Use the following command to build:

```
mvn clean install -DskipTests -Dgpg.skip=true
```
Introduction to KMS Client
----------

| KMS client Java classes | Introduction | Usage scenarios |
| :-----| :---- | :---- |
| com.aliyun.kms.kms20160120.Client | KMS resource management and key operations for KMS instance gateways are supported | 1. Scenarios where key operations are performed only through VPC gateways. <br>2. KMS resource management scenarios that only use public gateways. <br>3. Scenarios where you want to perform key operations through VPC gateways and manage KMS resources through public gateways.|
| com.aliyun.kms.kms20160120.TransferClient | Users can migrate from KMS 1.0 key operations to KMS 3.0 key operations | Users who use Alibaba Cloud SDK to access KMS 1.0 key operations need to migrate to KMS 3.0 |

Sample code
----------
### 1. Scenarios where key operations are performed only through VPC gateways.
#### Refer to the following sample code to call the KMS Encrypt API. For more API examples, see [operation samples](./examples/src/main/java/com/aliyun/kms/kms20160120/samples/operation)
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
### 2. KMS resources are managed only through public gateways.
#### Refer to the following sample code to call the KMS CreateKey API. For more API examples, see [manage samples](./examples/src/main/java/com/aliyun/kms/kms20160120/samples/manage)

```Java
public class CreateKey {

    public static com.aliyun.teaopenapi.models.Config createOpenApiConfig(String accessKeyId, String accessKeySecret, String regionId) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.accessKeyId = accessKeyId;
        config.accessKeySecret = accessKeySecret;
        config.regionId = regionId;
        return config;
    }

    public static com.aliyun.kms.kms20160120.Client createClient(com.aliyun.teaopenapi.models.Config openApiConfig) throws Exception {
        return new com.aliyun.kms.kms20160120.Client(openApiConfig);
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

    public static void main(String[] args_) throws Exception {
         //Make sure that the environment in which the code runs has environment variables ALIBABA_CLOUD_ACCESS_KEY_ID and ALIBABA_CLOUD_ACCESS_KEY_SECRET set.
         //Project code leakage may cause AccessKey to be leaked and threaten the security of all resources under the account. The following code example uses an environment variable to obtain the AccessKey for reference only, it is recommended to use the more secure STS mode, for more authentication access methods, see https://help.aliyun.com/document_detail/378657.html
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
### 3. You must not only perform key operations through a VPC gateway, but also manage KMS resources through a public gateway.
#### Refer to the following sample code to call the KMS CreateKey API and the Encrypt API. For more API examples, see [operation samples](./examples/src/main/java/com/aliyun/kms/kms20160120/samples/operation) and [manage samples](./examples/src/main/java/com/aliyun/kms/kms20160120/samples/manage)
```Java
public class Sample {

    public static com.aliyun.dkms.gcs.openapi.models.Config createKmsInstanceConfig(String clientKeyFile, String password, String endpoint, String caFilePath) throws Exception {
        com.aliyun.dkms.gcs.openapi.models.Config config = new com.aliyun.dkms.gcs.openapi.models.Config();
        config.clientKeyFile = clientKeyFile;
        config.password = password;
        config.endpoint = endpoint;
        config.caFilePath = caFilePath;
        return config;
    }

    public static com.aliyun.teaopenapi.models.Config createOpenApiConfig(String accessKeyId, String accessKeySecret, String regionId) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.accessKeyId = accessKeyId;
        config.accessKeySecret = accessKeySecret;
        config.regionId = regionId;
        return config;
    }

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
         //Make sure that the environment in which the code runs has environment variables ALIBABA_CLOUD_ACCESS_KEY_ID and ALIBABA_CLOUD_ACCESS_KEY_SECRET set.
         //Project code leakage may cause AccessKey to be leaked and threaten the security of all resources under the account. The following code example uses an environment variable to obtain the AccessKey for reference only, it is recommended to use the more secure STS mode, for more authentication access methods, see https://help.aliyun.com/document_detail/378657.html
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
### Users who uses Alibaba Cloud SDK to access KMS 1.0 keys need to migrate to access KMS 3.0 keys.
#### Refer to the following sample code to call the KMS API. For more API examples, see [kms transfer samples](./examples/src/main/java/com/aliyun/kms/kms20160120/samples/transfer)
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
        //set kms config
        Config config = new Config()
                //set the KMS shared gateway endpoint
                .setEndpoint("your-kms-endpoint")
                //set accessKeyId
                .setAccessKeyId(System.getenv("your-ak-env-name"))
                //set accessKeySecret
                .setAccessKeySecret(System.getenv("your-sk-env-name"));
        //set kms instance config
        com.aliyun.dkms.gcs.openapi.models.Config kmsConfig
                = new KmsConfig()
                //set the request protocol to HTTPS
                .setProtocol("https")
                //set client key file path
                .setClientKeyFile("your-client-key-file")
                //set client key password
                .setPassword("your-password")
                //set instance endpoint
                .setEndpoint("your-dkms-endpoint")
                //set your KMS instance's CA certificate with the file path
                .setCaFilePath("path/to/yourCaCert");
            // or, with content of CA certificate
            //.setCa("your-ca-certificate-content");
            //create kms client
            return new com.aliyun.kms.kms20160120.TransferClient(config, kmsConfig);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * create key
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
     * generate data key
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

Copyright (c) 2009-present, Alibaba Cloud All rights reserved.
