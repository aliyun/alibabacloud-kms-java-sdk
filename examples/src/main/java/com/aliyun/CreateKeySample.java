package com.aliyun;

import com.aliyun.kms.kms20160120.Client;
import com.aliyun.kms20160120.models.CreateKeyRequest;
import com.aliyun.kms20160120.models.CreateKeyResponse;
import com.aliyun.tea.TeaException;


public class CreateKeySample {
    public static void main(String[] args) throws Exception {
        createKey();
    }

    public static void createKey() {

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
            CreateKeyRequest request = new CreateKeyRequest();
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
}
