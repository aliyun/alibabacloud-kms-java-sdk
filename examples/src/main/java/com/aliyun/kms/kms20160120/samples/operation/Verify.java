// This file is auto-generated, don't edit it. Thanks.
package com.aliyun.kms.kms20160120.samples.operation;

import com.aliyun.tea.*;

public class Verify {

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

    public static com.aliyun.dkms.gcs.sdk.models.VerifyResponse verify(com.aliyun.kms.kms20160120.Client client, String messageType, byte[] signature, String keyId, byte[] message, String algorithm) throws Exception {
        com.aliyun.dkms.gcs.sdk.models.VerifyRequest request = new com.aliyun.dkms.gcs.sdk.models.VerifyRequest()
                .setMessageType(messageType)
                .setSignature(signature)
                .setKeyId(keyId)
                .setMessage(message)
                .setAlgorithm(algorithm);
        return client.verify(request);
    }

    public static void main(String[] args_) throws Exception {
        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dkms.gcs.openapi.models.Config kmsInstanceConfig = Verify.createKmsInstanceConfig(com.aliyun.darabonba.env.EnvClient.getEnv("your client key file path env"), com.aliyun.darabonba.env.EnvClient.getEnv("your client key password env"), "your kms instance endpoint env", "your ca file path");
        com.aliyun.kms.kms20160120.Client client = Verify.createClient(kmsInstanceConfig);
        String messageType = "your messageType";
        byte[] signature = com.aliyun.teautil.Common.toBytes("your signature");
        String keyId = "your keyId";
        byte[] message = com.aliyun.teautil.Common.toBytes("your message");
        String algorithm = "your algorithm";
        com.aliyun.dkms.gcs.sdk.models.VerifyResponse response = Verify.verify(client, messageType, signature, keyId, message, algorithm);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(response));
    }
}
