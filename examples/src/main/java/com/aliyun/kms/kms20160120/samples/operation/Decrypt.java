// This file is auto-generated, don't edit it. Thanks.
package com.aliyun.kms.kms20160120.samples.operation;

import com.aliyun.tea.*;

public class Decrypt {

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

    public static com.aliyun.dkms.gcs.sdk.models.DecryptResponse decrypt(com.aliyun.kms.kms20160120.Client client, String paddingMode, byte[] aad, byte[] ciphertextBlob, String keyId, byte[] iv, String algorithm) throws Exception {
        com.aliyun.dkms.gcs.sdk.models.DecryptRequest request = new com.aliyun.dkms.gcs.sdk.models.DecryptRequest()
                .setPaddingMode(paddingMode)
                .setAad(aad)
                .setCiphertextBlob(ciphertextBlob)
                .setKeyId(keyId)
                .setIv(iv)
                .setAlgorithm(algorithm);
        return client.decrypt(request);
    }

    public static void main(String[] args_) throws Exception {
        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dkms.gcs.openapi.models.Config kmsInstanceConfig = Decrypt.createKmsInstanceConfig(com.aliyun.darabonba.env.EnvClient.getEnv("your client key file path env"), com.aliyun.darabonba.env.EnvClient.getEnv("your client key password env"), "your kms instance endpoint env", "your ca file path");
        com.aliyun.kms.kms20160120.Client client = Decrypt.createClient(kmsInstanceConfig);
        String paddingMode = "your paddingMode";
        byte[] aad = com.aliyun.teautil.Common.toBytes("your aad");
        byte[] ciphertextBlob = com.aliyun.teautil.Common.toBytes("your ciphertextBlob");
        String keyId = "your keyId";
        byte[] iv = com.aliyun.teautil.Common.toBytes("your iv");
        String algorithm = "your algorithm";
        com.aliyun.dkms.gcs.sdk.models.DecryptResponse response = Decrypt.decrypt(client, paddingMode, aad, ciphertextBlob, keyId, iv, algorithm);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(response));
    }
}
