// This file is auto-generated, don't edit it. Thanks.
package com.aliyun.kms.kms20160120.samples.operation;

import com.aliyun.tea.*;

public class AdvanceGenerateDataKey {

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

    public static com.aliyun.dkms.gcs.sdk.models.AdvanceGenerateDataKeyResponse advanceGenerateDataKey(com.aliyun.kms.kms20160120.Client client, byte[] aad, String keyId, Integer numberOfBytes) throws Exception {
        com.aliyun.dkms.gcs.sdk.models.AdvanceGenerateDataKeyRequest request = new com.aliyun.dkms.gcs.sdk.models.AdvanceGenerateDataKeyRequest()
                .setAad(aad)
                .setKeyId(keyId)
                .setNumberOfBytes(numberOfBytes);
        return client.advanceGenerateDataKey(request);
    }

    public static void main(String[] args_) throws Exception {
        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dkms.gcs.openapi.models.Config kmsInstanceConfig = AdvanceGenerateDataKey.createKmsInstanceConfig(com.aliyun.darabonba.env.EnvClient.getEnv("your client key file path env"), com.aliyun.darabonba.env.EnvClient.getEnv("your client key password env"), "your kms instance endpoint env", "your ca file path");
        com.aliyun.kms.kms20160120.Client client = AdvanceGenerateDataKey.createClient(kmsInstanceConfig);
        byte[] aad = com.aliyun.teautil.Common.toBytes("your aad");
        String keyId = "your keyId";
        Integer numberOfBytes = com.aliyun.darabonbanumber.Client.parseInt(com.aliyun.teautil.Common.assertAsString("your numberOfBytes"));
        com.aliyun.dkms.gcs.sdk.models.AdvanceGenerateDataKeyResponse response = AdvanceGenerateDataKey.advanceGenerateDataKey(client, aad, keyId, numberOfBytes);
        com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(response));
    }
}
