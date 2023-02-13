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


Requirements
--------

- Java 1.8 or later
- Maven

Install
--------

The recommended way to use the Alibaba Cloud KMS SDK for Java in your project is to consume it from Maven. Import as follows:

```
<dependency>
    <groupId>com.aliyun.kms</groupId>
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

Alibaba Cloud KMS SDK for Java transfers the following methods of
request to the KMS instance vpc gateway by default.

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


You can use the Alibaba Cloud KMS Java SDK to forward the preceding interfaces to the KMS instance vpc gateway to the KMS shared gateway.
- Refer to the following code to forward calls from all of these interfaces to the KMS shared gateway.
```java
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
            //set kms config
            com.aliyun.dkms.gcs.openapi.models.Config kmsConfig
                    = new KmsConfig()
                    //set the SSL authentication identity, which is false by default, the SSL certificate needs to be verified. When set setIgnoreSSLVerifySwitch with true, you can set whether to ignore SSL certificates when calling the interface
                    .setIgnoreSSLVerifySwitch(false)
                    //set the request protocol to HTTPS
                    .setProtocol("https")
                    //set client key file path
                    .setClientKeyFile("your-client-key-file")
                    //set client key password
                    .setPassword("your-password")
                    //set instance endpoint
                    .setEndpoint("your-dkms-endpoint");
            //To verify the server-side certificate, you need to set it to your server-side certification path
            //.setCaFilePath("path/to/yourCaCert")
            // or, set the content of your server-side certificate
            //.setCa("your-ca-certificate-content"));
            //create a KMS client and forward all interfaces to the KMS shared gateway
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig,true);
            //create a GetSecretValue request body
            GetSecretValueRequest request = new GetSecretValueRequest();
            //set the SecretName parameter
            request.setSecretName("your-secret-name");
            //create KMS runtime configuration parameters
            RuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //to ignore SSL certificate authentication, run the following code and set ignoreSSLVerifySwitch to true
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
```
- Refer to the following code to transfer the GetSecretValue request to the KMS shared gateway.
```java
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.ApiNames;
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
            //set kms config
            com.aliyun.dkms.gcs.openapi.models.Config kmsConfig
                    = new KmsConfig()
                    // send the request with the related API name  to the kms share gateway
                    .setDefaultKmsApiNames(ApiNames.GET_SECRET_VALUE_API_NAME)
                    //set the SSL authentication identity, which is false by default, that is, the SSL certificate needs to be verified. When true, you can set whether to ignore SSL certificates when calling the interface
                    .setIgnoreSSLVerifySwitch(false)
                    //set the request protocol to HTTPS
                    .setProtocol("https")
                    //set client key file path
                    .setClientKeyFile("your-client-key-file")
                    //set client key password
                    .setPassword("your-password")
                    //set instance endpoint
                    .setEndpoint("your-dkms-endpoint");
            //To verify the server-side certificate, you need to set it to your server-side certification path
            //.setCaFilePath("path/to/yourCaCert")
            // or, set the content of your server-side certificate
            //.setCa("your-ca-certificate-content"));
            //create kms client
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //create a GetSecretValue request body
            GetSecretValueRequest request = new GetSecretValueRequest();
            //set the SecretName parameter
            request.setSecretName("your-secret-name");
            //create KMS runtime configuration parameters
            RuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //to ignore SSL certificate authentication, run the following code and set ignoreSSLVerifySwitch to true
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
```
- Refer to the following code to trasnfer a single request to the KMS shared gateway.
```java
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
            //set kms config
            Config config = new Config()
                    //set the KMS shared gateway endpoint
                    .setEndpoint("your-kms-endpoint")
                    //set accessKeyId
                    .setAccessKeyId(System.getenv("your-ak-env-name"))
                    //set accessKeySecret
                    .setAccessKeySecret(System.getenv("your-sk-env-name"));
            //set kms config
            com.aliyun.dkms.gcs.openapi.models.Config kmsConfig
                    = new KmsConfig()
                    //set the SSL authentication identity, which is false by default, that is, the SSL certificate needs to be verified. When true, you can set whether to ignore SSL certificates when calling the interface
                    .setIgnoreSSLVerifySwitch(false)
                    //set the request protocol to HTTPS
                    .setProtocol("https")
                    //set client key file path
                    .setClientKeyFile("your-client-key-file")
                    //set password
                    .setPassword("your-password")
                    //set instance endpoint
                    .setEndpoint("your-dkms-endpoint");
            //to verify the server-side certificate, you need to set it to your server-side certification path
            //.setCaFilePath("path/to/yourCaCert")
            //or, set the content of your server-side certificate
            //.setCa("your-ca-certificate-content"));
            //create a KMS client and forward all interfaces to the KMS shared gateway
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //create a GetSecretValue request body
            GetSecretValueRequest request = new GetSecretValueRequest();
            //set the SecretName parameter
            request.setSecretName("your-secret-name");
            //create KMS runtime configuration parameters
            KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //to ignore SSL certificate authentication, run the following code and set ignoreSSLVerifySwitch to true
            //runtimeOptions.ignoreSSL = true;
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
Sample code (using the GetSecretValue interface as an example)
----------
### You can call KMS services based on different scenarios
#### Scenario 1 The new user can refer to the following code to call the service of the KMS instance vpc gateway.
```Java
import com.aliyun.kms20160120.Client;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms20160120.models.GetSecretValueRequest;
import com.aliyun.kms20160120.models.GetSecretValueResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;

public class GetSecretValueSample {
    public static void main(String[] args) throws Exception {
        getSecretValue();
    }

    public static void getSecretValue() {
            try {
                //set kms config
                com.aliyun.dkms.gcs.openapi.models.Config config
                        = new KmsConfig()
                         //set the SSL authentication identity, which is false by default, that is, the SSL certificate needs to be verified. When true, you can set whether to ignore SSL certificates when calling the interface
                         .setIgnoreSSLVerifySwitch(false)
                        //set the request protocol to HTTPS
                        .setProtocol("https")
                        //set client key file path
                        .setClientKeyFile("your-client-key-file")
                        //set client key password
                        .setPassword("your-password")
                        //set instance endpoint
                        .setEndpoint("your-dkms-endpoint");
                //To verify the server-side certificate, you need to set it to your server-side certification path
                //.setCaFilePath("path/to/yourCaCert")
                // or, set the content of your server-side certificate
                //.setCa("your-ca-certificate-content"));
                //create kms client
                Client client = new com.aliyun.kms.kms20160120.Client(config);
                //create a GetSecretValue request body
                GetSecretValueRequest request = new GetSecretValueRequest();
                //set the SecretName parameter
                request.setSecretName("your-secret-name");
                //create KMS runtime configuration parameters
                RuntimeOptions runtimeOptions = new KmsRuntimeOptions();
                //to ignore SSL certificate authentication, run the following code and set ignoreSSLVerifySwitch to true
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
```
#### Scenario 2 Veteran users can refer to the following sample code of two different scenarios to call KMS services.
+ Solution 1 Before key migration, replace the old SDK (KMS20160120) with the cost SDK, and then use the KMS shared gateway to access KMS services.
      After the key is migrated, replace the KMS shared gateway with a KMS instance vpc gateway to access KMS services.
+ Solution 2 After key migration, replace the old SDK (KMS20160120) with the cost SDK and use the KMS instance vpc gateway to access KMS services.
##### The sample code before key migration is as follows:
```Java
import com.aliyun.kms20160120.Client;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
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
           //create KMS runtime configuration parameters
           RuntimeOptions runtimeOptions = new KmsRuntimeOptions();
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

##### The sample code after key migration is as follows:
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
            //set kms config
            Config config = new Config()
                    //set the KMS shared gateway endpoint
                    .setEndpoint("your-kms-endpoint")
                    //set accessKeyId
                    .setAccessKeyId(System.getenv("your-ak-env-name"))
                    //set accessKeySecret
                    .setAccessKeySecret(System.getenv("your-sk-env-name"));
            //set kms config
            com.aliyun.dkms.gcs.openapi.models.Config kmsConfig
                    = new KmsConfig()
                    //set the SSL authentication identity, which is false by default, that is, the SSL certificate needs to be verified. When true, you can set whether to ignore SSL certificates when calling the interface
                    .setIgnoreSSLVerifySwitch(false)
                    //set the request protocol to HTTPS
                    .setProtocol("https")
                    //set client key file path
                    .setClientKeyFile("your-client-key-file")
                    //set client key password
                    .setPassword("your-password")
                    //set instance endpoint
                    .setEndpoint("your-dkms-endpoint");
            //To verify the server-side certificate, you need to set it to your server-side certification path
            //.setCaFilePath("path/to/yourCaCert")
            // or, set the content of your server-side certificate
            //.setCa("your-ca-certificate-content"));
            //create kms client
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //create a GetSecretValue request body
            GetSecretValueRequest request = new GetSecretValueRequest();
            //set the SecretName parameter
            request.setSecretName("your-secret-name");
            //create KMS runtime configuration parameters
            RuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //to ignore SSL certificate authentication, run the following code and set ignoreSSLVerifySwitch to true
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
```
Character encoding setting instructions (default UTF-8)
----------
- You can refer to the following code example to set the global character set encoding.
```java
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
            //set kms config
            com.aliyun.dkms.gcs.openapi.models.Config kmsConfig
                    = new KmsConfig()
                    //set charset to UTF-8
                    .setCharset(StandardCharsets.UTF_8)
                    //set the SSL authentication identity, which is false by default, that is, the SSL certificate needs to be verified. When true, you can set whether to ignore SSL certificates when calling the interface
                    .setIgnoreSSLVerifySwitch(false)
                    //set the request protocol to HTTPS
                    .setProtocol("https")
                    //set client key file path
                    .setClientKeyFile("your-client-key-file")
                    //set client key password
                    .setPassword("your-password")
                    //set instance endpoint
                    .setEndpoint("your-dkms-endpoint");
            //To verify the server-side certificate, you need to set it to your server-side certification path
            //.setCaFilePath("path/to/yourCaCert")
            // or, set the content of your server-side certificate
            //.setCa("your-ca-certificate-content"));
            //create a KMS client and forward all interfaces to the KMS shared gateway
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //create a GetSecretValue request body
            GetSecretValueRequest request = new GetSecretValueRequest();
            //set the SecretName parameter
            request.setSecretName("your-secret-name");
            //create KMS runtime configuration parameters
            KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //to ignore SSL certificate authentication, run the following code and set ignoreSSLVerifySwitch to true
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
```
- You can refer to the following code example to set the character set encoding for a single request.
```java
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
            //set kms config
            com.aliyun.dkms.gcs.openapi.models.Config kmsConfig
                    = new KmsConfig()
                    //set the SSL authentication identity, which is false by default, that is, the SSL certificate needs to be verified. When true, you can set whether to ignore SSL certificates when calling the interface
                    .setIgnoreSSLVerifySwitch(false)
                    //set the request protocol to HTTPS
                    .setProtocol("https")
                    //set client key file path
                    .setClientKeyFile("your-client-key-file")
                    //set client key password
                    .setPassword("your-password")
                    //set instance endpoint
                    .setEndpoint("your-dkms-endpoint");
            //To verify the server-side certificate, you need to set it to your server-side certification path
            //.setCaFilePath("path/to/yourCaCert")
            // or, set the content of your server-side certificate
            //.setCa("your-ca-certificate-content"));
            //create a KMS client and forward all interfaces to the KMS shared gateway
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //create a GetSecretValue request body
            GetSecretValueRequest request = new GetSecretValueRequest();
            //set the SecretName parameter
            request.setSecretName("your-secret-name");
            //create KMS runtime configuration parameters
            KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //to ignore SSL certificate authentication, run the following code and set ignoreSSLVerifySwitch to true
            //runtimeOptions.ignoreSSL = true;
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

License
-------

[Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Copyright (c) 2009-present, Alibaba Cloud All rights reserved.
