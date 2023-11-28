// This file is auto-generated, don't edit it. Thanks.
package com.aliyun.kms.kms20160120;

import com.aliyun.kms.kms20160120.utils.Constants;
import com.aliyun.tea.TeaConverter;
import com.aliyun.tea.TeaModel;
import com.aliyun.tea.TeaPair;

public class Client extends com.aliyun.dkms.gcs.sdk.Client {

    public com.aliyun.kms20160120.Client _kmsClient;

    public Client(com.aliyun.dkms.gcs.openapi.models.Config kmsInstanceConfig, com.aliyun.teaopenapi.models.Config openApiConfig) throws Exception {
        super(kmsInstanceConfig);
        if (kmsInstanceConfig.getUserAgent() == null || "".equals(kmsInstanceConfig.getUserAgent())) {
            this._userAgent = com.aliyun.dkms.gcs.openapi.util.Client.getUserAgent(Constants.CLIENT_USER_AGENT);
        } else {
            this._userAgent = com.aliyun.dkms.gcs.openapi.util.Client.getUserAgent(kmsInstanceConfig.getUserAgent() + " " + Constants.CLIENT_USER_AGENT);
        }
        this._kmsClient = new com.aliyun.kms20160120.Client(openApiConfig);
    }

    public Client(com.aliyun.dkms.gcs.openapi.models.Config config) throws Exception {
        super(config);
        if (config.getUserAgent() == null || "".equals(config.getUserAgent())) {
            this._userAgent = com.aliyun.dkms.gcs.openapi.util.Client.getUserAgent(Constants.CLIENT_USER_AGENT);
        } else {
            this._userAgent = com.aliyun.dkms.gcs.openapi.util.Client.getUserAgent(config.getUserAgent() + " " + Constants.CLIENT_USER_AGENT);
        }
    }

    public Client(com.aliyun.teaopenapi.models.Config openApiConfig) throws Exception {
        super(new com.aliyun.dkms.gcs.openapi.models.Config().setEndpoint("mock endpoint"));
        this._kmsClient = new com.aliyun.kms20160120.Client(openApiConfig);
    }

    public <T extends TeaModel> T doAction(TeaModel request, String action, com.aliyun.teautil.models.RuntimeOptions runtime, Class<T> classOfT) throws Exception {
        com.aliyun.teautil.Common.validateModel(request);
        java.util.Map<String, Object> query = request.toMap();
        com.aliyun.teaopenapi.models.OpenApiRequest req = com.aliyun.teaopenapi.models.OpenApiRequest.build(TeaConverter.buildMap(
                new TeaPair("query", com.aliyun.openapiutil.Client.query(query))
        ));
        com.aliyun.teaopenapi.models.Params params = com.aliyun.teaopenapi.models.Params.build(TeaConverter.buildMap(
                new TeaPair("action", action),
                new TeaPair("version", "2016-01-20"),
                new TeaPair("protocol", "HTTPS"),
                new TeaPair("pathname", "/"),
                new TeaPair("method", "POST"),
                new TeaPair("authType", "AK"),
                new TeaPair("style", "RPC"),
                new TeaPair("reqBodyType", "formData"),
                new TeaPair("bodyType", "json")
        ));
        return TeaModel.toModel(this._kmsClient.callApi(params, req, runtime), classOfT.newInstance());
    }

    /**
     * 调用CancelKeyDeletion接口撤销密钥删除
     *
     * @param request
     * @return CancelKeyDeletionResponse
     */
    public com.aliyun.kms20160120.models.CancelKeyDeletionResponse cancelKeyDeletion(com.aliyun.kms20160120.models.CancelKeyDeletionRequest request) throws Exception {
        return _kmsClient.cancelKeyDeletion(request);
    }

    /**
     * 带运行参数调用CancelKeyDeletion接口撤销密钥删除
     *
     * @param request
     * @param runtime
     * @return CancelKeyDeletionResponse
     */
    public com.aliyun.kms20160120.models.CancelKeyDeletionResponse cancelKeyDeletionWithOptions(com.aliyun.kms20160120.models.CancelKeyDeletionRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.cancelKeyDeletionWithOptions(request, runtime);
    }

    /**
     * 调用CreateAlias接口为主密钥（CMK）创建一个别名
     *
     * @param request
     * @return CreateAliasResponse
     */
    public com.aliyun.kms20160120.models.CreateAliasResponse createAlias(com.aliyun.kms20160120.models.CreateAliasRequest request) throws Exception {
        return _kmsClient.createAlias(request);
    }

    /**
     * 带运行参数调用CreateAlias接口为主密钥（CMK）创建一个别名
     *
     * @param request
     * @param runtime
     * @return CreateAliasResponse
     */
    public com.aliyun.kms20160120.models.CreateAliasResponse createAliasWithOptions(com.aliyun.kms20160120.models.CreateAliasRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.createAliasWithOptions(request, runtime);
    }

    /**
     * 调用CreateKey接口创建一个主密钥
     *
     * @param request
     * @return CreateKeyResponse
     */
    public com.aliyun.kms20160120.models.CreateKeyResponse createKey(com.aliyun.kms20160120.models.CreateKeyRequest request) throws Exception {
        return _kmsClient.createKey(request);
    }

    /**
     * 带运行参数调用CreateKey接口创建一个主密钥
     *
     * @param request
     * @param runtime
     * @return CreateKeyResponse
     */
    public com.aliyun.kms20160120.models.CreateKeyResponse createKeyWithOptions(com.aliyun.kms20160120.models.CreateKeyRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.createKeyWithOptions(request, runtime);
    }

    /**
     * 调用CreateKeyVersion接口为用户主密钥（CMK）创建密钥版本
     *
     * @param request
     * @return CreateKeyVersionResponse
     */
    public com.aliyun.kms20160120.models.CreateKeyVersionResponse createKeyVersion(com.aliyun.kms20160120.models.CreateKeyVersionRequest request) throws Exception {
        return _kmsClient.createKeyVersion(request);
    }

    /**
     * 带运行参数调用CreateKeyVersion接口为用户主密钥（CMK）创建密钥版本
     *
     * @param request
     * @param runtime
     * @return CreateKeyVersionResponse
     */
    public com.aliyun.kms20160120.models.CreateKeyVersionResponse createKeyVersionWithOptions(com.aliyun.kms20160120.models.CreateKeyVersionRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.createKeyVersionWithOptions(request, runtime);
    }

    /**
     * 创建凭据并存入凭据的初始版本
     *
     * @param request
     * @return CreateSecretResponse
     */
    public com.aliyun.kms20160120.models.CreateSecretResponse createSecret(com.aliyun.kms20160120.models.CreateSecretRequest request) throws Exception {
        return _kmsClient.createSecret(request);
    }

    /**
     * 带运行参数创建凭据并存入凭据的初始版本
     *
     * @param request
     * @param runtime
     * @return CreateSecretResponse
     */
    public com.aliyun.kms20160120.models.CreateSecretResponse createSecretWithOptions(com.aliyun.kms20160120.models.CreateSecretRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.createSecretWithOptions(request, runtime);
    }

    /**
     * 调用DeleteAlias接口删除别名
     *
     * @param request
     * @return DeleteAliasResponse
     */
    public com.aliyun.kms20160120.models.DeleteAliasResponse deleteAlias(com.aliyun.kms20160120.models.DeleteAliasRequest request) throws Exception {
        return _kmsClient.deleteAlias(request);
    }

    /**
     * 带运行参数调用DeleteAlias接口删除别名
     *
     * @param request
     * @param runtime
     * @return DeleteAliasResponse
     */
    public com.aliyun.kms20160120.models.DeleteAliasResponse deleteAliasWithOptions(com.aliyun.kms20160120.models.DeleteAliasRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.deleteAliasWithOptions(request, runtime);
    }

    /**
     * 调用DeleteKeyMaterial接口删除已导入的密钥材料
     *
     * @param request
     * @return DeleteKeyMaterialResponse
     */
    public com.aliyun.kms20160120.models.DeleteKeyMaterialResponse deleteKeyMaterial(com.aliyun.kms20160120.models.DeleteKeyMaterialRequest request) throws Exception {
        return _kmsClient.deleteKeyMaterial(request);
    }

    /**
     * 带运行参数调用DeleteKeyMaterial接口删除已导入的密钥材料
     *
     * @param request
     * @param runtime
     * @return DeleteKeyMaterialResponse
     */
    public com.aliyun.kms20160120.models.DeleteKeyMaterialResponse deleteKeyMaterialWithOptions(com.aliyun.kms20160120.models.DeleteKeyMaterialRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.deleteKeyMaterialWithOptions(request, runtime);
    }

    /**
     * 调用DeleteSecret接口删除凭据对象
     *
     * @param request
     * @return DeleteSecretResponse
     */
    public com.aliyun.kms20160120.models.DeleteSecretResponse deleteSecret(com.aliyun.kms20160120.models.DeleteSecretRequest request) throws Exception {
        return _kmsClient.deleteSecret(request);
    }

    /**
     * 带运行参数调用DeleteSecret接口删除凭据对象
     *
     * @param request
     * @param runtime
     * @return DeleteSecretResponse
     */
    public com.aliyun.kms20160120.models.DeleteSecretResponse deleteSecretWithOptions(com.aliyun.kms20160120.models.DeleteSecretRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.deleteSecretWithOptions(request, runtime);
    }

    /**
     * 调用DescribeKey接口查询用户主密钥（CMK）详情
     *
     * @param request
     * @return DescribeKeyResponse
     */
    public com.aliyun.kms20160120.models.DescribeKeyResponse describeKey(com.aliyun.kms20160120.models.DescribeKeyRequest request) throws Exception {
        return _kmsClient.describeKey(request);
    }

    /**
     * 带运行参数调用DescribeKey接口查询用户主密钥（CMK）详情
     *
     * @param request
     * @param runtime
     * @return DescribeKeyResponse
     */
    public com.aliyun.kms20160120.models.DescribeKeyResponse describeKeyWithOptions(com.aliyun.kms20160120.models.DescribeKeyRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.describeKeyWithOptions(request, runtime);
    }

    /**
     * 调用DescribeKeyVersion接口查询指定密钥版本信息
     *
     * @param request
     * @return DescribeKeyVersionResponse
     */
    public com.aliyun.kms20160120.models.DescribeKeyVersionResponse describeKeyVersion(com.aliyun.kms20160120.models.DescribeKeyVersionRequest request) throws Exception {
        return _kmsClient.describeKeyVersion(request);
    }

    /**
     * 带运行参数调用DescribeKeyVersion接口查询指定密钥版本信息
     *
     * @param request
     * @param runtime
     * @return DescribeKeyVersionResponse
     */
    public com.aliyun.kms20160120.models.DescribeKeyVersionResponse describeKeyVersionWithOptions(com.aliyun.kms20160120.models.DescribeKeyVersionRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.describeKeyVersionWithOptions(request, runtime);
    }

    /**
     * 调用DescribeSecret接口查询凭据的元数据信息
     *
     * @param request
     * @return DescribeSecretResponse
     */
    public com.aliyun.kms20160120.models.DescribeSecretResponse describeSecret(com.aliyun.kms20160120.models.DescribeSecretRequest request) throws Exception {
        return _kmsClient.describeSecret(request);
    }

    /**
     * 带运行参数调用DescribeSecret接口查询凭据的元数据信息
     *
     * @param request
     * @param runtime
     * @return DescribeSecretResponse
     */
    public com.aliyun.kms20160120.models.DescribeSecretResponse describeSecretWithOptions(com.aliyun.kms20160120.models.DescribeSecretRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.describeSecretWithOptions(request, runtime);
    }

    /**
     * 调用DisableKey接口禁用指定的主密钥（CMK）进行加解密
     *
     * @param request
     * @return DisableKeyResponse
     */
    public com.aliyun.kms20160120.models.DisableKeyResponse disableKey(com.aliyun.kms20160120.models.DisableKeyRequest request) throws Exception {
        return _kmsClient.disableKey(request);
    }

    /**
     * 带运行参数调用DisableKey接口禁用指定的主密钥（CMK）进行加解密
     *
     * @param request
     * @param runtime
     * @return DisableKeyResponse
     */
    public com.aliyun.kms20160120.models.DisableKeyResponse disableKeyWithOptions(com.aliyun.kms20160120.models.DisableKeyRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.disableKeyWithOptions(request, runtime);
    }

    /**
     * 调用EnableKey接口启用指定的主密钥进行加解密
     *
     * @param request
     * @return EnableKeyResponse
     */
    public com.aliyun.kms20160120.models.EnableKeyResponse enableKey(com.aliyun.kms20160120.models.EnableKeyRequest request) throws Exception {
        return _kmsClient.enableKey(request);
    }

    /**
     * 带运行参数调用EnableKey接口启用指定的主密钥进行加解密
     *
     * @param request
     * @param runtime
     * @return EnableKeyResponse
     */
    public com.aliyun.kms20160120.models.EnableKeyResponse enableKeyWithOptions(com.aliyun.kms20160120.models.EnableKeyRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.enableKeyWithOptions(request, runtime);
    }

    /**
     * 调用GetParametersForImport接口获取导入主密钥材料的参数
     *
     * @param request
     * @return GetParametersForImportResponse
     */
    public com.aliyun.kms20160120.models.GetParametersForImportResponse getParametersForImport(com.aliyun.kms20160120.models.GetParametersForImportRequest request) throws Exception {
        return _kmsClient.getParametersForImport(request);
    }

    /**
     * 带运行参数调用GetParametersForImport接口获取导入主密钥材料的参数
     *
     * @param request
     * @param runtime
     * @return GetParametersForImportResponse
     */
    public com.aliyun.kms20160120.models.GetParametersForImportResponse getParametersForImportWithOptions(com.aliyun.kms20160120.models.GetParametersForImportRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.getParametersForImportWithOptions(request, runtime);
    }

    /**
     * 调用GetRandomPassword接口获得一个随机口令字符串
     *
     * @param request
     * @return GetRandomPasswordResponse
     */
    public com.aliyun.kms20160120.models.GetRandomPasswordResponse getRandomPassword(com.aliyun.kms20160120.models.GetRandomPasswordRequest request) throws Exception {
        return _kmsClient.getRandomPassword(request);
    }

    /**
     * 带运行参数调用GetRandomPassword接口获得一个随机口令字符串
     *
     * @param request
     * @param runtime
     * @return GetRandomPasswordResponse
     */
    public com.aliyun.kms20160120.models.GetRandomPasswordResponse getRandomPasswordWithOptions(com.aliyun.kms20160120.models.GetRandomPasswordRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.getRandomPasswordWithOptions(request, runtime);
    }

    /**
     * 调用ImportKeyMaterial接口导入密钥材料
     *
     * @param request
     * @return ImportKeyMaterialResponse
     */
    public com.aliyun.kms20160120.models.ImportKeyMaterialResponse importKeyMaterial(com.aliyun.kms20160120.models.ImportKeyMaterialRequest request) throws Exception {
        return _kmsClient.importKeyMaterial(request);
    }

    /**
     * 带运行参数调用ImportKeyMaterial接口导入密钥材料
     *
     * @param request
     * @param runtime
     * @return ImportKeyMaterialResponse
     */
    public com.aliyun.kms20160120.models.ImportKeyMaterialResponse importKeyMaterialWithOptions(com.aliyun.kms20160120.models.ImportKeyMaterialRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.importKeyMaterialWithOptions(request, runtime);
    }

    /**
     * 调用ListAliases接口查询当前用户在当前地域的所有别名
     *
     * @param request
     * @return ListAliasesResponse
     */
    public com.aliyun.kms20160120.models.ListAliasesResponse listAliases(com.aliyun.kms20160120.models.ListAliasesRequest request) throws Exception {
        return _kmsClient.listAliases(request);
    }

    /**
     * 带运行参数调用ListAliases接口查询当前用户在当前地域的所有别名
     *
     * @param request
     * @param runtime
     * @return ListAliasesResponse
     */
    public com.aliyun.kms20160120.models.ListAliasesResponse listAliasesWithOptions(com.aliyun.kms20160120.models.ListAliasesRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.listAliasesWithOptions(request, runtime);
    }

    /**
     * 调用ListKeys查询调用者在调用地域的所有主密钥ID
     *
     * @param request
     * @return ListKeysResponse
     */
    public com.aliyun.kms20160120.models.ListKeysResponse listKeys(com.aliyun.kms20160120.models.ListKeysRequest request) throws Exception {
        return _kmsClient.listKeys(request);
    }

    /**
     * 带运行参数调用ListKeys查询调用者在调用地域的所有主密钥ID
     *
     * @param request
     * @param runtime
     * @return ListKeysResponse
     */
    public com.aliyun.kms20160120.models.ListKeysResponse listKeysWithOptions(com.aliyun.kms20160120.models.ListKeysRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.listKeysWithOptions(request, runtime);
    }

    /**
     * 调用ListKeyVersions接口列出主密钥的所有密钥版本
     *
     * @param request
     * @return ListKeyVersionsResponse
     */
    public com.aliyun.kms20160120.models.ListKeyVersionsResponse listKeyVersions(com.aliyun.kms20160120.models.ListKeyVersionsRequest request) throws Exception {
        return _kmsClient.listKeyVersions(request);
    }

    /**
     * 带运行参数调用ListKeyVersions接口列出主密钥的所有密钥版本
     *
     * @param request
     * @param runtime
     * @return ListKeyVersionsResponse
     */
    public com.aliyun.kms20160120.models.ListKeyVersionsResponse listKeyVersionsWithOptions(com.aliyun.kms20160120.models.ListKeyVersionsRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.listKeyVersionsWithOptions(request, runtime);
    }

    /**
     * 调用ListResourceTags获取用户主密钥的标签
     *
     * @param request
     * @return ListResourceTagsResponse
     */
    public com.aliyun.kms20160120.models.ListResourceTagsResponse listResourceTags(com.aliyun.kms20160120.models.ListResourceTagsRequest request) throws Exception {
        return _kmsClient.listResourceTags(request);
    }

    /**
     * 带运行参数调用ListResourceTags获取用户主密钥的标签
     *
     * @param request
     * @param runtime
     * @return ListResourceTagsResponse
     */
    public com.aliyun.kms20160120.models.ListResourceTagsResponse listResourceTagsWithOptions(com.aliyun.kms20160120.models.ListResourceTagsRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.listResourceTagsWithOptions(request, runtime);
    }

    /**
     * 调用ListSecrets接口查询当前用户在当前地域创建的所有凭据
     *
     * @param request
     * @return ListSecretsResponse
     */
    public com.aliyun.kms20160120.models.ListSecretsResponse listSecrets(com.aliyun.kms20160120.models.ListSecretsRequest request) throws Exception {
        return _kmsClient.listSecrets(request);
    }

    /**
     * 带运行参数调用ListSecrets接口查询当前用户在当前地域创建的所有凭据
     *
     * @param request
     * @param runtime
     * @return ListSecretsResponse
     */
    public com.aliyun.kms20160120.models.ListSecretsResponse listSecretsWithOptions(com.aliyun.kms20160120.models.ListSecretsRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.listSecretsWithOptions(request, runtime);
    }

    /**
     * 调用ListSecretVersionIds接口查询凭据的所有版本信息
     *
     * @param request
     * @return ListSecretVersionIdsResponse
     */
    public com.aliyun.kms20160120.models.ListSecretVersionIdsResponse listSecretVersionIds(com.aliyun.kms20160120.models.ListSecretVersionIdsRequest request) throws Exception {
        return _kmsClient.listSecretVersionIds(request);
    }

    /**
     * 带运行参数调用ListSecretVersionIds接口查询凭据的所有版本信息
     *
     * @param request
     * @param runtime
     * @return ListSecretVersionIdsResponse
     */
    public com.aliyun.kms20160120.models.ListSecretVersionIdsResponse listSecretVersionIdsWithOptions(com.aliyun.kms20160120.models.ListSecretVersionIdsRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.listSecretVersionIdsWithOptions(request, runtime);
    }

    /**
     * 调用PutSecretValue接口为凭据存入一个新版本的凭据值
     *
     * @param request
     * @return PutSecretValueResponse
     */
    public com.aliyun.kms20160120.models.PutSecretValueResponse putSecretValue(com.aliyun.kms20160120.models.PutSecretValueRequest request) throws Exception {
        return _kmsClient.putSecretValue(request);
    }

    /**
     * 带运行参数调用PutSecretValue接口为凭据存入一个新版本的凭据值
     *
     * @param request
     * @param runtime
     * @return PutSecretValueResponse
     */
    public com.aliyun.kms20160120.models.PutSecretValueResponse putSecretValueWithOptions(com.aliyun.kms20160120.models.PutSecretValueRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.putSecretValueWithOptions(request, runtime);
    }

    /**
     * 调用RestoreSecret接口恢复被删除的凭据
     *
     * @param request
     * @return RestoreSecretResponse
     */
    public com.aliyun.kms20160120.models.RestoreSecretResponse restoreSecret(com.aliyun.kms20160120.models.RestoreSecretRequest request) throws Exception {
        return _kmsClient.restoreSecret(request);
    }

    /**
     * 带运行参数调用RestoreSecret接口恢复被删除的凭据
     *
     * @param request
     * @param runtime
     * @return RestoreSecretResponse
     */
    public com.aliyun.kms20160120.models.RestoreSecretResponse restoreSecretWithOptions(com.aliyun.kms20160120.models.RestoreSecretRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.restoreSecretWithOptions(request, runtime);
    }

    /**
     * 调用RotateSecret接口手动轮转凭据
     *
     * @param request
     * @return RotateSecretResponse
     */
    public com.aliyun.kms20160120.models.RotateSecretResponse rotateSecret(com.aliyun.kms20160120.models.RotateSecretRequest request) throws Exception {
        return _kmsClient.rotateSecret(request);
    }

    /**
     * 带运行参数调用RotateSecret接口手动轮转凭据
     *
     * @param request
     * @param runtime
     * @return RotateSecretResponse
     */
    public com.aliyun.kms20160120.models.RotateSecretResponse rotateSecretWithOptions(com.aliyun.kms20160120.models.RotateSecretRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.rotateSecretWithOptions(request, runtime);
    }

    /**
     * 调用ScheduleKeyDeletion接口申请删除一个指定的主密钥（CMK)
     *
     * @param request
     * @return ScheduleKeyDeletionResponse
     */
    public com.aliyun.kms20160120.models.ScheduleKeyDeletionResponse scheduleKeyDeletion(com.aliyun.kms20160120.models.ScheduleKeyDeletionRequest request) throws Exception {
        return _kmsClient.scheduleKeyDeletion(request);
    }

    /**
     * 带运行参数调用ScheduleKeyDeletion接口申请删除一个指定的主密钥（CMK)
     *
     * @param request
     * @param runtime
     * @return ScheduleKeyDeletionResponse
     */
    public com.aliyun.kms20160120.models.ScheduleKeyDeletionResponse scheduleKeyDeletionWithOptions(com.aliyun.kms20160120.models.ScheduleKeyDeletionRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.scheduleKeyDeletionWithOptions(request, runtime);
    }

    /**
     * 调用SetDeletionProtection接口为用户主密钥（CMK）开启或关闭删除保护
     *
     * @param request
     * @return SetDeletionProtectionResponse
     */
    public com.aliyun.kms20160120.models.SetDeletionProtectionResponse setDeletionProtection(com.aliyun.kms20160120.models.SetDeletionProtectionRequest request) throws Exception {
        return _kmsClient.setDeletionProtection(request);
    }

    /**
     * 带运行参数调用SetDeletionProtection接口为用户主密钥（CMK）开启或关闭删除保护
     *
     * @param request
     * @param runtime
     * @return SetDeletionProtectionResponse
     */
    public com.aliyun.kms20160120.models.SetDeletionProtectionResponse setDeletionProtectionWithOptions(com.aliyun.kms20160120.models.SetDeletionProtectionRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.setDeletionProtectionWithOptions(request, runtime);
    }

    /**
     * 调用TagResource接口为主密钥、凭据或证书绑定标签
     *
     * @param request
     * @return TagResourceResponse
     */
    public com.aliyun.kms20160120.models.TagResourceResponse tagResource(com.aliyun.kms20160120.models.TagResourceRequest request) throws Exception {
        return _kmsClient.tagResource(request);
    }

    /**
     * 带运行参数调用TagResource接口为主密钥、凭据或证书绑定标签
     *
     * @param request
     * @param runtime
     * @return TagResourceResponse
     */
    public com.aliyun.kms20160120.models.TagResourceResponse tagResourceWithOptions(com.aliyun.kms20160120.models.TagResourceRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.tagResourceWithOptions(request, runtime);
    }

    /**
     * 调用UntagResource接口为主密钥、凭据或证书解绑标签
     *
     * @param request
     * @return UntagResourceResponse
     */
    public com.aliyun.kms20160120.models.UntagResourceResponse untagResource(com.aliyun.kms20160120.models.UntagResourceRequest request) throws Exception {
        return _kmsClient.untagResource(request);
    }

    /**
     * 带运行参数调用UntagResource接口为主密钥、凭据或证书解绑标签
     *
     * @param request
     * @param runtime
     * @return UntagResourceResponse
     */
    public com.aliyun.kms20160120.models.UntagResourceResponse untagResourceWithOptions(com.aliyun.kms20160120.models.UntagResourceRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.untagResourceWithOptions(request, runtime);
    }

    /**
     * 调用UpdateAlias接口更新已存在的别名所代表的主密钥（CMK）ID
     *
     * @param request
     * @return UpdateAliasResponse
     */
    public com.aliyun.kms20160120.models.UpdateAliasResponse updateAlias(com.aliyun.kms20160120.models.UpdateAliasRequest request) throws Exception {
        return _kmsClient.updateAlias(request);
    }

    /**
     * 带运行参数调用UpdateAlias接口更新已存在的别名所代表的主密钥（CMK）ID
     *
     * @param request
     * @param runtime
     * @return UpdateAliasResponse
     */
    public com.aliyun.kms20160120.models.UpdateAliasResponse updateAliasWithOptions(com.aliyun.kms20160120.models.UpdateAliasRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.updateAliasWithOptions(request, runtime);
    }

    /**
     * 调用UpdateKeyDescription接口更新主密钥的描述信息
     *
     * @param request
     * @return UpdateKeyDescriptionResponse
     */
    public com.aliyun.kms20160120.models.UpdateKeyDescriptionResponse updateKeyDescription(com.aliyun.kms20160120.models.UpdateKeyDescriptionRequest request) throws Exception {
        return _kmsClient.updateKeyDescription(request);
    }

    /**
     * 带运行参数调用UpdateKeyDescription接口更新主密钥的描述信息
     *
     * @param request
     * @param runtime
     * @return UpdateKeyDescriptionResponse
     */
    public com.aliyun.kms20160120.models.UpdateKeyDescriptionResponse updateKeyDescriptionWithOptions(com.aliyun.kms20160120.models.UpdateKeyDescriptionRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.updateKeyDescriptionWithOptions(request, runtime);
    }

    /**
     * 调用UpdateRotationPolicy接口更新密钥轮转策略
     *
     * @param request
     * @return UpdateRotationPolicyResponse
     */
    public com.aliyun.kms20160120.models.UpdateRotationPolicyResponse updateRotationPolicy(com.aliyun.kms20160120.models.UpdateRotationPolicyRequest request) throws Exception {
        return _kmsClient.updateRotationPolicy(request);
    }

    /**
     * 带运行参数调用UpdateRotationPolicy接口更新密钥轮转策略
     *
     * @param request
     * @param runtime
     * @return UpdateRotationPolicyResponse
     */
    public com.aliyun.kms20160120.models.UpdateRotationPolicyResponse updateRotationPolicyWithOptions(com.aliyun.kms20160120.models.UpdateRotationPolicyRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.updateRotationPolicyWithOptions(request, runtime);
    }

    /**
     * 调用UpdateSecret接口更新凭据的元数据
     *
     * @param request
     * @return UpdateSecretResponse
     */
    public com.aliyun.kms20160120.models.UpdateSecretResponse updateSecret(com.aliyun.kms20160120.models.UpdateSecretRequest request) throws Exception {
        return _kmsClient.updateSecret(request);
    }

    /**
     * 带运行参数调用UpdateSecret接口更新凭据的元数据
     *
     * @param request
     * @param runtime
     * @return UpdateSecretResponse
     */
    public com.aliyun.kms20160120.models.UpdateSecretResponse updateSecretWithOptions(com.aliyun.kms20160120.models.UpdateSecretRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.updateSecretWithOptions(request, runtime);
    }

    /**
     * 调用UpdateSecretRotationPolicy接口更新凭据轮转策略
     *
     * @param request
     * @return UpdateSecretRotationPolicyResponse
     */
    public com.aliyun.kms20160120.models.UpdateSecretRotationPolicyResponse updateSecretRotationPolicy(com.aliyun.kms20160120.models.UpdateSecretRotationPolicyRequest request) throws Exception {
        return _kmsClient.updateSecretRotationPolicy(request);
    }

    /**
     * 带运行参数调用UpdateSecretRotationPolicy接口更新凭据轮转策略
     *
     * @param request
     * @param runtime
     * @return UpdateSecretRotationPolicyResponse
     */
    public com.aliyun.kms20160120.models.UpdateSecretRotationPolicyResponse updateSecretRotationPolicyWithOptions(com.aliyun.kms20160120.models.UpdateSecretRotationPolicyRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.updateSecretRotationPolicyWithOptions(request, runtime);
    }

    /**
     * 调用UpdateSecretVersionStage接口更新凭据的版本状态
     *
     * @param request
     * @return UpdateSecretVersionStageResponse
     */
    public com.aliyun.kms20160120.models.UpdateSecretVersionStageResponse updateSecretVersionStage(com.aliyun.kms20160120.models.UpdateSecretVersionStageRequest request) throws Exception {
        return _kmsClient.updateSecretVersionStage(request);
    }

    /**
     * 带运行参数调用UpdateSecretVersionStage接口更新凭据的版本状态
     *
     * @param request
     * @param runtime
     * @return UpdateSecretVersionStageResponse
     */
    public com.aliyun.kms20160120.models.UpdateSecretVersionStageResponse updateSecretVersionStageWithOptions(com.aliyun.kms20160120.models.UpdateSecretVersionStageRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.updateSecretVersionStageWithOptions(request, runtime);
    }

    /**
     * 调用OpenKmsService接口为当前阿里云账号开通密钥管理服务
     *
     * @return OpenKmsServiceResponse
     */
    public com.aliyun.kms20160120.models.OpenKmsServiceResponse openKmsService() throws Exception {
        return _kmsClient.openKmsService();
    }

    /**
     * 带运行参数调用OpenKmsService接口为当前阿里云账号开通密钥管理服务
     *
     * @param runtime
     * @return OpenKmsServiceResponse
     */
    public com.aliyun.kms20160120.models.OpenKmsServiceResponse openKmsServiceWithOptions(com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.openKmsServiceWithOptions(runtime);
    }

    /**
     * 调用DescribeRegions接口查询当前账号的可用地域列表
     *
     * @return DescribeRegionsResponse
     */
    public com.aliyun.kms20160120.models.DescribeRegionsResponse describeRegions() throws Exception {
        return _kmsClient.describeRegions();
    }

    /**
     * 带运行参数调用DescribeRegions接口查询当前账号的可用地域列表
     *
     * @param runtime
     * @return DescribeRegionsResponse
     */
    public com.aliyun.kms20160120.models.DescribeRegionsResponse describeRegionsWithOptions(com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.describeRegionsWithOptions(runtime);
    }

    /**
     * 调用DescribeAccountKmsStatus接口查询当前阿里云账号的密钥管理服务状态
     *
     * @return DescribeAccountKmsStatusResponse
     */
    public com.aliyun.kms20160120.models.DescribeAccountKmsStatusResponse describeAccountKmsStatus() throws Exception {
        return _kmsClient.describeAccountKmsStatus();
    }

    /**
     * 带运行参数调用DescribeAccountKmsStatus接口查询当前阿里云账号的密钥管理服务状态
     *
     * @param runtime
     * @return DescribeAccountKmsStatusResponse
     */
    public com.aliyun.kms20160120.models.DescribeAccountKmsStatusResponse describeAccountKmsStatusWithOptions(com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.describeAccountKmsStatusWithOptions(runtime);
    }

    /**
     * 调用GetSecretValue接口获取共享网关凭据值
     *
     * @param request
     * @return GetSecretValueResponse
     */
    public com.aliyun.kms20160120.models.GetSecretValueResponse getSecretValueBySharedEndpoint(com.aliyun.kms20160120.models.GetSecretValueRequest request) throws Exception {
        return _kmsClient.getSecretValue(request);
    }

    /**
     * 带运行参数调用GetSecretValue接口获取共享网关凭据值
     *
     * @param request
     * @param runtime
     * @return GetSecretValueResponse
     */
    public com.aliyun.kms20160120.models.GetSecretValueResponse getSecretValueWithOptionsBySharedEndpoint(com.aliyun.kms20160120.models.GetSecretValueRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.getSecretValueWithOptions(request, runtime);
    }

    /**
     * 调用GetPublicKey接口获取共享网关非对称密钥的公钥
     *
     * @param request
     * @return GetPublicKeyResponse
     */
    public com.aliyun.kms20160120.models.GetPublicKeyResponse getPublicKeyBySharedEndpoint(com.aliyun.kms20160120.models.GetPublicKeyRequest request) throws Exception {
        return _kmsClient.getPublicKey(request);
    }

    /**
     * 带运行参数调用GetPublicKey接口获取共享网关非对称密钥的公钥
     *
     * @param request
     * @param runtime
     * @return GetPublicKeyResponse
     */
    public com.aliyun.kms20160120.models.GetPublicKeyResponse getPublicKeyWithOptionsBySharedEndpoint(com.aliyun.kms20160120.models.GetPublicKeyRequest request, com.aliyun.teautil.models.RuntimeOptions runtime) throws Exception {
        return _kmsClient.getPublicKeyWithOptions(request, runtime);
    }
}
