// This file is auto-generated, don't edit it. Thanks.
package com.aliyun.kms.kms20160120.samples.operation;

import com.aliyun.tea.*;

public class GetSecretValue {

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

    public static com.aliyun.dkms.gcs.sdk.models.GetSecretValueResponse getSecretValue(com.aliyun.kms.kms20160120.Client client, Boolean fetchExtendedConfig, String secretName, String versionId, String versionStage) throws Exception {
        com.aliyun.dkms.gcs.sdk.models.GetSecretValueRequest request = new com.aliyun.dkms.gcs.sdk.models.GetSecretValueRequest()
                .setFetchExtendedConfig(fetchExtendedConfig)
                .setSecretName(secretName)
                .setVersionId(versionId)
                .setVersionStage(versionStage);
        return client.getSecretValue(request);
    }

    public static void main(String[] args_) throws Exception {
        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dkms.gcs.openapi.models.Config kmsInstanceConfig = GetSecretValue.createKmsInstanceConfig(com.aliyun.darabonba.env.EnvClient.getEnv("your client key file path env"), com.aliyun.darabonba.env.EnvClient.getEnv("your client key password env"), "your kms instance endpoint env", "your ca file path");
        com.aliyun.kms.kms20160120.Client client = GetSecretValue.createClient(kmsInstanceConfig);
        Boolean fetchExtendedConfig = false;
        String secretName = "your secretName";
        String versionId = "your versionId";
        String versionStage = "your versionStage";
        com.aliyun.dkms.gcs.sdk.models.GetSecretValueResponse response = GetSecretValue.getSecretValue(client, fetchExtendedConfig, secretName, versionId, versionStage);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(response));
    }
}
