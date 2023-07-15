// This file is auto-generated, don't edit it. Thanks.
package com.aliyun.kms.kms20160120.samples.manage;

import com.aliyun.tea.*;

public class GetPublicKeyBySharedEndpoint {

    public static com.aliyun.teaopenapi.models.Config createOpenApiConfig(String accessKeyId, String accessKeySecret, String regionId) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setRegionId(regionId);
        return config;
    }

    public static com.aliyun.kms.kms20160120.Client createClient(com.aliyun.teaopenapi.models.Config openApiConfig) throws Exception {
        return new com.aliyun.kms.kms20160120.Client(openApiConfig);
    }

    public static com.aliyun.kms20160120.models.GetPublicKeyResponse getPublicKeyBySharedEndpoint(com.aliyun.kms.kms20160120.Client client, String keyVersionId, String keyId) throws Exception {
        com.aliyun.kms20160120.models.GetPublicKeyRequest request = new com.aliyun.kms20160120.models.GetPublicKeyRequest()
                .setKeyVersionId(keyVersionId)
                .setKeyId(keyId);
        return client.getPublicKeyBySharedEndpoint(request);
    }

    public static void main(String[] args_) throws Exception {
        java.util.List<String> args = java.util.Arrays.asList(args_);
        // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        com.aliyun.teaopenapi.models.Config openApiConfig = GetPublicKeyBySharedEndpoint.createOpenApiConfig(com.aliyun.darabonba.env.EnvClient.getEnv("ALIBABA_CLOUD_ACCESS_KEY_ID"), com.aliyun.darabonba.env.EnvClient.getEnv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"), "your region id");
        com.aliyun.kms.kms20160120.Client client = GetPublicKeyBySharedEndpoint.createClient(openApiConfig);
        String keyVersionId = "your keyVersionId";
        String keyId = "your keyId";
        com.aliyun.kms20160120.models.GetPublicKeyResponse response = GetPublicKeyBySharedEndpoint.getPublicKeyBySharedEndpoint(client, keyVersionId, keyId);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(response));
    }
}
