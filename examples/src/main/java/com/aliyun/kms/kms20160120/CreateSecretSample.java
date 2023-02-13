package com.aliyun.kms.kms20160120;

import com.aliyun.kms.kms20160120.Client;
import com.aliyun.kms20160120.models.CreateSecretRequest;
import com.aliyun.kms20160120.models.CreateSecretResponse;
import com.aliyun.tea.TeaException;


public class CreateSecretSample {
    public static void main(String[] args) throws Exception {
        createSecret();
    }

    public static void createSecret() {

        try {
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
            CreateSecretRequest request = new CreateSecretRequest();
            request.setSecretName("your-secret-name");
            request.setSecretData("your-secret-data");
            CreateSecretResponse response = client.createSecret(request);
            System.out.printf("SecretName: %s%n", response.getBody().getSecretName());
            System.out.printf("ExtendedConfig: %s%n", response.getBody().getExtendedConfig());
            System.out.printf("VersionId: %s%n", response.getBody().getVersionId());
        } catch (TeaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
