// This file is auto-generated, don't edit it. Thanks.
package com.aliyun.kms.kms20160120.samples.operation;

import com.aliyun.tea.*;

public class GetPublicKey {

    public static com.aliyun.dkms.gcs.openapi.models.Config createKmsInstanceConfig(String clientKeyFile, String password, String endpoint, String caFilePath) throws Exception {
        com.aliyun.dkms.gcs.openapi.models.Config config = new com.aliyun.dkms.gcs.openapi.models.Config()
                .setClientKeyFile(clientKeyFile)
                .setPassword(password)
                .setEndpoint(endpoint)
                .setCaFilePath(caFilePath);
        return config;
    }

    public static com.aliyun.kms.kms20160120.Client createClient(com.aliyun.dkms.gcs.openapi.models.Config kmsInstanceConfig) throws Exception {
        return new com.aliyun.kms.kms20160120.Client(kmsInstanceConfig);
    }

    public static com.aliyun.dkms.gcs.sdk.models.GetPublicKeyResponse getPublicKey(com.aliyun.kms.kms20160120.Client client, String keyId) throws Exception {
        com.aliyun.dkms.gcs.sdk.models.GetPublicKeyRequest request = new com.aliyun.dkms.gcs.sdk.models.GetPublicKeyRequest()
                .setKeyId(keyId);
        return client.getPublicKey(request);
    }

    public static void main(String[] args_) throws Exception {
        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dkms.gcs.openapi.models.Config kmsInstanceConfig = GetPublicKey.createKmsInstanceConfig(com.aliyun.darabonba.env.EnvClient.getEnv("your client key file path env"), com.aliyun.darabonba.env.EnvClient.getEnv("your client key password env"), "your kms instance endpoint env", "your ca file path");
        com.aliyun.kms.kms20160120.Client client = GetPublicKey.createClient(kmsInstanceConfig);
        String keyId = "your keyId";
        com.aliyun.dkms.gcs.sdk.models.GetPublicKeyResponse response = GetPublicKey.getPublicKey(client, keyId);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(response));
    }
}
