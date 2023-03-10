Alibaba Cloud KMS SDK for Java
=================================

![image0](https://aliyunsdk-pages.alicdn.com/icons/AlibabaCloud.svg)

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

- Access KMS services through the KMS shared gateway
- Access KMS services through the KMS instance gateway

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
    <version>1.1.0</version>
</dependency>
```


Build
--------

Once you check out the code from GitHub, you can build it using Maven. Use the following command to build:

```
mvn clean install -DskipTests -Dgpg.skip=true
```

Client Mechanism
----------------
Alibaba Cloud KMS Java SDK supports to call APIs provided by the KMS shared gateway and KMS instance gateway.
By default, Alibaba Cloud KMS Java SDK sends requests for the following APIs to the KMS instance gateway, and the other APIs to the KMS shared gateway.
-   Encrypt
-   Decrypt
-   GenerateDataKey
-   GenerateDataKeyWithoutPlaintext
-   GetPublicKey
-   AsymmetricEncrypt
-   AsymmetricDecrypt
-   AsymmetricSign
-   AsymmetricVerify
-   GetSecretValue


Alibaba Cloud KMS Java SDK also supports sending API requests from the above APIs to the KMS shared gateway. For details, see Sample code - Special Usage scenarios.

Sample Code
----------
### Common usage scenarios
#### Scenario 1 You can refer to the following code to call APIs of the KMS shared gateway and the KMS instance gateway in the default way.
```Java
import com.aliyun.kms.kms20160120.Client;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms20160120.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;

public class Sample {
    public static void main(String[] args) throws Exception {
        Client client = initClient();
        createKey(client);
        generateDataKey(client);
    }

    private static Client initClient() {
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
            return new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
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
#### Scenario 2 You can refer to the following code to call only the APIs of the KMS instance gateway.
```Java
import com.aliyun.kms20160120.Client;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms20160120.models.GetSecretValueRequest;
import com.aliyun.kms20160120.models.GetSecretValueResponse;
import com.aliyun.tea.TeaException;

public class GetSecretValueSample {
    public static void main(String[] args) throws Exception {
        getSecretValue();
    }

    public static void getSecretValue() {
            try {
                //set kms instance config
                com.aliyun.dkms.gcs.openapi.models.Config config
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
                Client client = new com.aliyun.kms.kms20160120.Client(config);
                //create a GetSecretValue request body
                GetSecretValueRequest request = new GetSecretValueRequest();
                //set the SecretName parameter
                request.setSecretName("your-secret-name");
                GetSecretValueResponse response = client.getSecretValue(request);
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
```
#### Scenario 3 You can refer to the following code to call only the KMS shared gateway API.
```Java
import com.aliyun.kms20160120.Client;
import com.aliyun.kms20160120.models.GetSecretValueRequest;
import com.aliyun.kms20160120.models.GetSecretValueResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
   
   
public class GetSecretValueSample {
   public static void main(String[] args) throws Exception {
       getSecretValue();
   }

   public static void getSecretValue() {

       try {
            //set kms config
           Config config = new Config()
                   //set the KMS shared gateway endpoint
                   .setEndpoint("your-kms-endpoint")
                   //set accessKeyId
                   .setAccessKeyId(System.getenv("your-ak-env-name"))
                   //set accessKeySecret
                   .setAccessKeySecret(System.getenv("your-sk-env-name"));
           //create kms client
           Client client = new com.aliyun.kms.kms20160120.Client(config);
           //create a GetSecretValue request body
           GetSecretValueRequest request = new GetSecretValueRequest();
           //set the SecretName parameter
           request.setSecretName("your-secret-name");
           GetSecretValueResponse response = client.getSecretValue(request);
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
```
### Special usage scenarios
#### Scenario 1 Refer to the following code to forward calls from all of these API to the KMS shared gateway.
```Java
import com.aliyun.kms20160120.Client;
import com.aliyun.kms20160120.models.GetSecretValueRequest;
import com.aliyun.kms20160120.models.GetSecretValueResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;


public class GetSecretValueSample {
    public static void main(String[] args) throws Exception {
        getSecretValue();
    }

    public static void getSecretValue() {

        try {
            //set config
            Config config = new Config()
                    //set the KMS shared gateway endpoint
                    .setEndpoint("your-kms-endpoint")
                    //set accessKeyId
                    .setAccessKeyId(System.getenv("your-ak-env-name"))
                    //set accessKeySecret
                    .setAccessKeySecret(System.getenv("your-sk-env-name"));

            //create a KMS client and forward all interfaces to the KMS shared gateway
            Client client = new com.aliyun.kms.kms20160120.Client(config);
            //create a GetSecretValue request body
            GetSecretValueRequest request = new GetSecretValueRequest();
            //set the SecretName parameter
            request.setSecretName("your-secret-name");

            GetSecretValueResponse response = client.getSecretValue(request);
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
```
#### Scenario 2 Refer to the following code to forward the call to a specific API (GetSecretValue) to the KMS shared gateway.
```Java
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.utils.ApiNames;
import com.aliyun.kms20160120.Client;
import com.aliyun.kms20160120.models.GetSecretValueRequest;
import com.aliyun.kms20160120.models.GetSecretValueResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;


public class GetSecretValueSample {
    public static void main(String[] args) throws Exception {
        getSecretValue();
    }

    public static void getSecretValue() {

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
                    // send the request with the related API name  to the kms share gateway
                    .setDefaultKmsApiNames(ApiNames.GET_SECRET_VALUE_API_NAME)
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
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //create a GetSecretValue request body
            GetSecretValueRequest request = new GetSecretValueRequest();
            //set the SecretName parameter
            request.setSecretName("your-secret-name");

            GetSecretValueResponse response = client.getSecretValue(request);
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
```
#### Scenario 3 Refer to the following code to forward a single call to the KMS shared gateway.
```Java
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms20160120.Client;
import com.aliyun.kms20160120.models.GetSecretValueRequest;
import com.aliyun.kms20160120.models.GetSecretValueResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;


public class GetSecretValueSample {
    public static void main(String[] args) throws Exception {
        getSecretValue();
    }

    public static void getSecretValue() {

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
                    //set password
                    .setPassword("your-password")
                    //set instance endpoint
                    .setEndpoint("your-dkms-endpoint")
                    //set your KMS instance's CA certificate with the file path
                    .setCaFilePath("path/to/yourCaCert");
                // or, with content of CA certificate
            //.setCa("your-ca-certificate-content");
            //create a KMS client and forward all interfaces to the KMS shared gateway
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //create a GetSecretValue request body
            GetSecretValueRequest request = new GetSecretValueRequest();
            //set the SecretName parameter
            request.setSecretName("your-secret-name");
            //create KMS runtime configuration parameters
            KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //configure this request interface to forward to the KMS shared gateway
            runtimeOptions.setIsUseKmsShareGateway(true);
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
```
Character encoding setting instructions (default UTF-8)
----------
- You can refer to the following code example to set the global character set encoding.
```Java
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms20160120.Client;
import com.aliyun.kms20160120.models.GetSecretValueRequest;
import com.aliyun.kms20160120.models.GetSecretValueResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;

import java.nio.charset.StandardCharsets;


public class Sample {
    public static void main(String[] args) throws Exception {
        getSecretValue();
    }

    public static void getSecretValue() {

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
                    //set charset to UTF-8
                    .setCharset(StandardCharsets.UTF_8)
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
            //create a KMS client and forward all interfaces to the KMS shared gateway
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //create a GetSecretValue request body
            GetSecretValueRequest request = new GetSecretValueRequest();
            //set the SecretName parameter
            request.setSecretName("your-secret-name");
            GetSecretValueResponse response = client.getSecretValue(request);
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
```
- You can refer to the following code example to set the character set encoding for a single request.
```Java
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms20160120.Client;
import com.aliyun.kms20160120.models.GetSecretValueRequest;
import com.aliyun.kms20160120.models.GetSecretValueResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;

import java.nio.charset.StandardCharsets;


public class Sample {
    public static void main(String[] args) throws Exception {
        getSecretValue();
    }

    public static void getSecretValue() {

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
            //create a KMS client and forward all interfaces to the KMS shared gateway
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //create a GetSecretValue request body
            GetSecretValueRequest request = new GetSecretValueRequest();
            //set the SecretName parameter
            request.setSecretName("your-secret-name");
            //create KMS runtime configuration parameters
            KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //set charset to UTF-8
            runtimeOptions.setCharset(StandardCharsets.UTF_8);
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
```

SSL certificate validation switch setting (default validation of SSL certificate)
----------
You can refer to the following code example to set the HTTPS SSL certificate not to be validated, for example when developing tests, to simplify the program.

```Java
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms20160120.Client;
import com.aliyun.kms20160120.models.GetSecretValueRequest;
import com.aliyun.kms20160120.models.GetSecretValueResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;


public class GetSecretValueSample {
    public static void main(String[] args) throws Exception {
        getSecretValue();
    }

    public static void getSecretValue() {

        try {
            //set config
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
                    //set the SSL authentication identity, which is false by default, the SSL certificate needs to be verified. When set setIgnoreSSLVerifySwitch with true, you can set whether to ignore SSL certificates when calling the interface
                    .setIgnoreSSLVerifySwitch(true)
                    //set the request protocol to HTTPS
                    .setProtocol("https")
                    //set client key file path
                    .setClientKeyFile("your-client-key-file")
                    //set client key password
                    .setPassword("your-password")
                    //set instance endpoint
                    .setEndpoint("your-dkms-endpoint");
            			// you do not need to set your KMS instance's CA certificate when ingoring the SSL certificate validation
                  //.setCaFilePath("path/to/yourCaCert");
             // or, with content of CA certificate
            //.setCa("your-ca-certificate-content");
            //create a KMS client and forward all interfaces to the KMS shared gateway
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig,true);
            //create a GetSecretValue request body
            GetSecretValueRequest request = new GetSecretValueRequest();
            //set the SecretName parameter
            request.setSecretName("your-secret-name");
            //create KMS runtime configuration parameters
            RuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //SSL certificate authentication is ignored if ignoreSSLVerifySwitch is true
            runtimeOptions.ignoreSSL = true;
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
```

Copyright (c) 2009-present, Alibaba Cloud All rights reserved.
