package com.aliyun;

import com.aliyun.kms.kms20160120.Client;
import com.aliyun.kms20160120.models.*;
import com.aliyun.teautil.models.RuntimeOptions;
import com.aliyun.utils.ConfigUtils;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Properties;


public class TestClient {

    Properties properties;
    Client client;

    @Before
    public void init() throws Exception {
        properties = ConfigUtils.loadParam("");
        com.aliyun.dkms.gcs.openapi.models.Config config = new com.aliyun.dkms.gcs.openapi.models.Config()
                .setClientKeyContent(properties.getProperty("config.clientKeyContent"))
                .setPassword(properties.getProperty("config.password"))
                .setEndpoint(properties.getProperty("config.endpoint"))
                .setCaFilePath(properties.getProperty("config.caFilePath"));
        client = new com.aliyun.kms.kms20160120.Client(config);
    }

    @Test
    public void testKmsGetSecretValue() throws Exception {
        GetSecretValueRequest request = new GetSecretValueRequest();
        request.setSecretName(properties.getProperty("secret.name"));
        RuntimeOptions runtimeOptions = new RuntimeOptions();
        GetSecretValueResponse response = client.doAction(request, "GetSecretValue", runtimeOptions, GetSecretValueResponse.class);
        System.out.println(new Gson().toJson(response));
    }

    @Test
    public void testDkmsGetSecretValue() throws Exception {
        com.aliyun.dkms.gcs.sdk.models.GetSecretValueRequest request = new com.aliyun.dkms.gcs.sdk.models.GetSecretValueRequest();
        request.setSecretName(properties.getProperty("secret.name"));
        com.aliyun.dkms.gcs.sdk.models.GetSecretValueResponse response = client.getSecretValue(request);
        System.out.println(new Gson().toJson(response));
    }

    @Test
    public void testCreateSecret() throws Exception {
        CreateSecretRequest request = new CreateSecretRequest();
        request.setSecretName(properties.getProperty("secret.name") + "0");
        request.setSecretData("{\"test\":\"123\"}");
        request.setVersionId("1");
        CreateSecretResponse response = client.createSecret(request);
        System.out.println(new Gson().toJson(response));
    }
}