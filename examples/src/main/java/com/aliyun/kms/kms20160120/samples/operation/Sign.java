// This file is auto-generated, don't edit it. Thanks.
package com.aliyun.kms.kms20160120.samples.operation;

import com.aliyun.tea.*;

public class Sign {

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

    public static com.aliyun.dkms.gcs.sdk.models.SignResponse sign(com.aliyun.kms.kms20160120.Client client, String messageType, String keyId, byte[] message, String algorithm) throws Exception {
        com.aliyun.dkms.gcs.sdk.models.SignRequest request = new com.aliyun.dkms.gcs.sdk.models.SignRequest()
                .setMessageType(messageType)
                .setKeyId(keyId)
                .setMessage(message)
                .setAlgorithm(algorithm);
        return client.sign(request);
    }

    public static void main(String[] args_) throws Exception {
        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dkms.gcs.openapi.models.Config kmsInstanceConfig = Sign.createKmsInstanceConfig(com.aliyun.darabonba.env.EnvClient.getEnv("your client key file path env"), com.aliyun.darabonba.env.EnvClient.getEnv("your client key password env"), "your kms instance endpoint env", "your ca file path");
        com.aliyun.kms.kms20160120.Client client = Sign.createClient(kmsInstanceConfig);
        String messageType = "your messageType";
        String keyId = "your keyId";
        byte[] message = com.aliyun.teautil.Common.toBytes("your message");
        String algorithm = "your algorithm";
        com.aliyun.dkms.gcs.sdk.models.SignResponse response = Sign.sign(client, messageType, keyId, message, algorithm);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(response));
    }
}
