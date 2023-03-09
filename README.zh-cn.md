阿里云KMS Java SDK
=====================

![image0](https://aliyunsdk-pages.alicdn.com/icons/AlibabaCloud.svg)

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
- 通过KMS共享网关访问KMS OpenAPI
- 通过KMS实例网关访问KMS实例提供的API

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
    <version>1.1.0</version>
</dependency>
```

构建
----------

你可以从Github检出代码通过下面的maven命令进行构建。

```
mvn clean install -DskipTests -Dgpg.skip=true
```

客户端机制
----------

阿里云KMS Java SDK默认情况下会将以下方法的请求发送到KMS实例网关，其它KMS接口则发送到KMS共享网关。

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

阿里云KMS Java SDK也支持将以上接口的API请求发送到KMS共享网关，具体方法请参考使用示例-特殊使用场景。

使用示例
----------
### 常规使用场景
#### 场景一 可以参考下面的代码同时调用KMS共享网关和KMS实例网关的服务。
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
            return new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
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
#### 场景二 可以参考下面的代码仅调用KMS实例网关的服务。
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
            //创建调用KMS实例网关的config并设置相应参数
            com.aliyun.dkms.gcs.openapi.models.Config config
                    = new KmsConfig()
                    //设置请求协议为https
                    .setProtocol("https")
                    //设置dkms域名
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
            Client client = new com.aliyun.kms.kms20160120.Client(config);
            //创建GetSecretValue请求体
            GetSecretValueRequest request = new GetSecretValueRequest();
            //设置SecretName参数
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
#### 场景三 可以参考下面的代码仅调用KMS共享网关的服务。
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
           //创建调用KMS共享网关的config并设置相应参数
           Config config = new Config()
                   //设置KMS共享网关的域名
                   .setEndpoint("your-kms-endpoint")
                   //设置访问凭证AccessKeyId
                   .setAccessKeyId(System.getenv("your-ak-env-name"))
                   //设置访问凭证AccessKeySecret
                   .setAccessKeySecret(System.getenv("your-sk-env-name"));
                   
           //创建kms client
           Client client = new com.aliyun.kms.kms20160120.Client(config);
           //创建GetSecretValue请求体
           GetSecretValueRequest request = new GetSecretValueRequest();
           //设置SecretName参数
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
### 特殊使用场景
#### 场景一 参考如下代码将上述所有接口的调用请求发送到KMS共享网关。
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
                    
            //创建kms client,设置所有接口发送到KMS共享网关
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig,true);
            //创建GetSecretValue请求体
            GetSecretValueRequest request = new GetSecretValueRequest();
            //设置SecretName参数
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
#### 场景二 参考如下代码将调用GetSecretValue接口请求发送到KMS共享网关。
```java
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
                    //设置指定的API接口转发到KMS共享网关
                    .setDefaultKmsApiNames(ApiNames.GET_SECRET_VALUE_API_NAME)
                    //设置请求协议为https
                    .setProtocol("https")
                    //设置client key文件地址
                    .setClientKeyFile("your-client-key-file")
                    //设置client-key密码
                    .setPassword("your-password")
                    //设置dkms域名
                    .setEndpoint("your-dkms-endpoint")
                    // 设置KMS实例的CA证书。通过提供文件路径
                    .setCaFilePath("path/to/yourCaCert");
                    // 或者，CA证书内容。
                    //.setCa("your-ca-certificate-content"));
                    
            //创建kms client
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //创建GetSecretValue请求体
            GetSecretValueRequest request = new GetSecretValueRequest();
            //设置SecretName参数
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
#### 场景三 参考如下代码将单独一次调用请求发送到KMS共享网关。
```java
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
                    .setPassword("your-password")                    
                    // 设置KMS实例的CA证书。通过提供文件路径
                    .setCaFilePath("path/to/yourCaCert");
                    // 或者，CA证书内容。
                    //.setCa("your-ca-certificate-content");
            //创建kms client
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //创建GetSecretValue请求体
            GetSecretValueRequest request = new GetSecretValueRequest();
            //设置SecretName参数
            request.setSecretName("your-secret-name");
            
            //创建运行时配置参数
            KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();

            //设置此次请求接口发送到KMS共享网关
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

字符编码设置说明(默认为UTF-8)
----------
- 您可以参考以下代码示例，设置全局的字符集编码。
```java
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
                    //设置字符集编码为UTF-8
                    .setCharset(StandardCharsets.UTF_8)
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
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //创建GetSecretValue请求体
            GetSecretValueRequest request = new GetSecretValueRequest();
            //设置SecretName参数
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
- 您可以参考以下代码示例，设置单独一次请求的字符集编码。
```java
import com.aliyun.kms.kms20160120.model.KmsConfig;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms20160120.Client;
import com.aliyun.kms20160120.models.GetSecretValueRequest;
import com.aliyun.kms20160120.models.GetSecretValueResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

import java.nio.charset.StandardCharsets;


public class Sample {
    public static void main(String[] args) throws Exception {
        getSecretValue();
    }

    public static void getSecretValue() {

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
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //创建GetSecretValue请求体
            GetSecretValueRequest request = new GetSecretValueRequest();
            //设置SecretName参数
            request.setSecretName("your-secret-name");
            //创建kms 运行时配置参数
            KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();           
            //设置字符集编码为UTF-8
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

SSL证书验证开关设置(默认验证SSL证书)
----------
您可以参考以下代码示例，设置不验证HTTPS SSL证书，例如在开发测试时，以简化程序。

``` Java
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
                //创建kms config并设置相应参数
                com.aliyun.dkms.gcs.openapi.models.Config config
                        = new KmsConfig()
                         //设置ssl验证标识,默认为false,即需验证ssl证书;为true时,可在调用接口时设置是否忽略ssl证书
                         .setIgnoreSSLVerifySwitch(true)
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
                Client client = new com.aliyun.kms.kms20160120.Client(config);
                //创建GetSecretValue请求体
                GetSecretValueRequest request = new GetSecretValueRequest();
                //设置SecretName参数
                request.setSecretName("your-secret-name");
                //创建kms 运行时配置参数
                RuntimeOptions runtimeOptions = new KmsRuntimeOptions();
                //忽略SSL证书认证,此设置前提是ignoreSSLVerifySwitch为true
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

版权所有 2009-present, 阿里巴巴集团.
