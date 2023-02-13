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
    <version>1.0.0</version>
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

阿里云KMS Java SDK默认情况下会将以下方法的请求转发到KMS实例网关.

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

您可以使用阿里云KMS Java SDK将以上的转发到KMS实例网关的接口转发到KMS共享网关。
- 参考如下代码将上述所有接口的调用转发到KMS共享网关。
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
            //创建kms config并设置相应参数
            Config config = new Config()
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
            //创建kms client,设置所有接口转发到KMS共享网关
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig,true);
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
```
- 参考如下代码将调用GetSecretValue转发到KMS共享网关。
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
            //创建kms config并设置相应参数
            Config config = new Config()
                    //设置KMS共享网关的域名
                    .setEndpoint("your-kms-endpoint")
                    //设置访问凭证AccessKeyId
                    .setAccessKeyId(System.getenv("your-ak-env-name"))
                    //设置访问凭证AccessKeySecret
                    .setAccessKeySecret(System.getenv("your-sk-env-name"));
            //创建kms config并设置相应参数
            com.aliyun.dkms.gcs.openapi.models.Config kmsConfig
                    = new KmsConfig()
                    //设置指定的API接口转发到KMS共享网关
                    .setDefaultKmsApiNames(ApiNames.GET_SECRET_VALUE_API_NAME)
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
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
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
```
- 参考如下代码将单独一次调用转发到KMS共享网关。
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
            //创建kms config并设置相应参数
            Config config = new Config()
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
            //创建kms client,设置所有接口转发到KMS共享网关
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //创建GetSecretValue请求体
            GetSecretValueRequest request = new GetSecretValueRequest();
            //设置SecretName参数
            request.setSecretName("your-secret-name");
            //创建kms 运行时配置参数
            KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //如需忽略SSL证书认证,可打开如下代码并设置ignoreSSLVerifySwitch为true
            //runtimeOptions.ignoreSSL = true;
            //设置此次请求接口转发到KMS共享网关
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
示例代码(以GetSecretValue接口为例)
----------
### 用户可根据不同的场景选择参考示例调用KMS服务
#### 场景一 新接入用户可以参考下面的代码调用KMS实例网关的服务。
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
                //创建kms client
                Client client = new com.aliyun.kms.kms20160120.Client(config);
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
```
#### 场景二 老用户可参考以下两种不同方案的示例代码调用KMS服务。
+ 方案一 在密钥迁移前，先将旧的sdk(kms20160120)替换成本sdk，然后依然使用KMS共享网关访问KMS服务，示例代码参考密钥迁移前。
        在密钥迁移后，再将KMS共享网关替换成KMS实例网关访问KMS服务，示例代码参考密钥迁移后。
+ 方案二 在密钥迁移后，直接将旧的sdk(kms20160120)替换成本sdk，使用KMS实例网关访问KMS服务，示例代码参考密钥迁移后。
##### 密钥迁移前示例代码如下：
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
            //创建kms config并设置相应参数
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
           //创建kms 运行时配置参数
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

##### 密钥迁移后示例代码如下:
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
            //创建kms config并设置相应参数
            Config config = new Config()
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
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
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
```
字符编码设置说明(默认为UTF-8)
----------
- 您可以参考以下代码示例，设置全局的字符集编码。
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
            //创建kms config并设置相应参数
            Config config = new Config()
                    //设置KMS共享网关的域名
                    .setEndpoint("your-kms-endpoint")
                    //设置访问凭证AccessKeyId
                    .setAccessKeyId(System.getenv("your-ak-env-name"))
                    //设置访问凭证AccessKeySecret
                    .setAccessKeySecret(System.getenv("your-sk-env-name"));
            //创建kms config并设置相应参数
            com.aliyun.dkms.gcs.openapi.models.Config kmsConfig
                    = new KmsConfig()
                    //设置字符集编码为UTF-8
                    .setCharset(StandardCharsets.UTF_8)
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
            //创建kms client,设置所有接口转发到KMS共享网关
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //创建GetSecretValue请求体
            GetSecretValueRequest request = new GetSecretValueRequest();
            //设置SecretName参数
            request.setSecretName("your-secret-name");
            //创建kms 运行时配置参数
            KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();
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

import java.nio.charset.StandardCharsets;


public class Sample {
    public static void main(String[] args) throws Exception {
        getSecretValue();
    }

    public static void getSecretValue() {

        try {
            //创建kms config并设置相应参数
            Config config = new Config()
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
            //创建kms client,设置所有接口转发到KMS共享网关
            Client client = new com.aliyun.kms.kms20160120.Client(config, kmsConfig);
            //创建GetSecretValue请求体
            GetSecretValueRequest request = new GetSecretValueRequest();
            //设置SecretName参数
            request.setSecretName("your-secret-name");
            //创建kms 运行时配置参数
            KmsRuntimeOptions runtimeOptions = new KmsRuntimeOptions();
            //如需忽略SSL证书认证,可打开如下代码并设置ignoreSSLVerifySwitch为true
            //runtimeOptions.ignoreSSL = true;
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

许可证
------

[Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0)

版权所有 2009-present, 阿里巴巴集团.
