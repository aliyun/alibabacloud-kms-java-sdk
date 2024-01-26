package com.aliyun.kms.kms20160120.handlers;

import com.aliyun.dkms.gcs.openapi.Client;
import com.aliyun.kms.kms20160120.model.KmsRuntimeOptions;
import com.aliyun.kms.kms20160120.utils.KmsErrorCodeTransferUtils;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaModel;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.OpenApiRequest;
import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aliyun.kms.kms20160120.utils.Constants.*;
import static com.aliyun.kms.kms20160120.utils.KmsErrorCodeTransferUtils.*;


public interface KmsTransferHandler<DKMSRequest extends TeaModel, DKMSResponse extends TeaModel> {

    Base64 base64 = new Base64();
    List<String> responseHeaders = new ArrayList<String>() {{
        add(MIGRATION_KEY_VERSION_ID_KEY);
    }};

    default Map<String, ?> calApi(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException {
        try {
            return transferToOpenApiResponse(callDKMS(buildDKMSRequest(request, runtimeOptions), runtimeOptions), runtimeOptions);
        } catch (TeaException e) {
            throw transferTeaException(e);
        } catch (Exception e) {
            throw new TeaException("", e);
        }

    }

    <T extends TeaModel> DKMSRequest buildDKMSRequest(OpenApiRequest request, KmsRuntimeOptions runtimeOptions) throws TeaException;

    DKMSResponse callDKMS(DKMSRequest dkmsRequest, KmsRuntimeOptions runtimeOptions) throws Exception;

    Map<String, ?> transferToOpenApiResponse(DKMSResponse response, KmsRuntimeOptions runtimeOptions) throws TeaException;

    Client getClient();

    String getAction();

    default TeaException transferTeaException(TeaException e) {
        switch (e.getCode()) {
            case INVALID_PARAM_ERROR_CODE: {
                if (INVALID_PARAM_DATE_ERROR_MESSAGE.equals(e.getMessage())) {
                    transferInvalidDateException(e);
                    return e;
                } else if (INVALID_PARAM_AUTHORIZATION_ERROR_MESSAGE.equals(e.getMessage())) {
                    transferIncompleteSignatureException(e);
                    return e;
                }
            }
            case UNAUTHORIZED_ERROR_CODE: {
                transferInvalidAccessKeyIdException(e);
                return e;
            }
            default:
                String errorMessage = KmsErrorCodeTransferUtils.transferErrorMessage(e.getCode());
                errorMessage = StringUtils.isEmpty(errorMessage) ? e.getMessage() : errorMessage;
                e.setMessage(errorMessage);
                Map<String, Object> data = e.getData();
                if (data != null && data.size() > 0) {
                    data.put("Code", e.getCode());
                    data.put("Message", e.getMessage());
                }
                return e;
        }
    }

    default TeaException newMissingParameterClientException(String paramName) {
        TeaException e = new TeaException(new HashMap<String, Object>() {
            {
                put("code", KmsErrorCodeTransferUtils.MISSING_PARAMETER_ERROR_CODE);
                put("message", String.format("The parameter  %s  needed but no provided.", paramName));
            }
        });
        Map<String, Object> data = e.getData();
        if (data != null && data.size() > 0) {
            data.put("Code", e.getCode());
            data.put("Message", e.getMessage());
        }
        return e;
    }

    default TeaException newInvalidParameterClientException(String paramName) {
        TeaException e = new TeaException(new HashMap<String, Object>() {
            {
                put("code", KmsErrorCodeTransferUtils.INVALID_PARAMETER_ERROR_CODE);
                put("message", String.format("The parameter  %s  is invalid.", paramName));
            }
        });
        Map<String, Object> data = e.getData();
        if (data != null && data.size() > 0) {
            data.put("Code", e.getCode());
            data.put("Message", e.getMessage());
        }
        return e;
    }

    default com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions transferRuntimeOptions(KmsRuntimeOptions runtimeOptions) {
        com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions dkmsRuntimeOptions = new com.aliyun.dkms.gcs.openapi.util.models.RuntimeOptions();
        if (runtimeOptions != null) {
            dkmsRuntimeOptions.setIgnoreSSL(runtimeOptions.ignoreSSL);
            dkmsRuntimeOptions.setAutoretry(runtimeOptions.autoretry);
            dkmsRuntimeOptions.setBackoffPeriod(runtimeOptions.backoffPeriod);
            dkmsRuntimeOptions.setBackoffPolicy(runtimeOptions.backoffPolicy);
            dkmsRuntimeOptions.setConnectTimeout(runtimeOptions.connectTimeout);
            dkmsRuntimeOptions.setHttpProxy(runtimeOptions.httpProxy);
            dkmsRuntimeOptions.setHttpsProxy(runtimeOptions.httpsProxy);
            dkmsRuntimeOptions.setMaxAttempts(runtimeOptions.maxAttempts);
            dkmsRuntimeOptions.setMaxIdleConns(runtimeOptions.maxIdleConns);
            dkmsRuntimeOptions.setNoProxy(runtimeOptions.noProxy);
            dkmsRuntimeOptions.setReadTimeout(runtimeOptions.readTimeout);
            dkmsRuntimeOptions.setSocks5NetWork(runtimeOptions.socks5NetWork);
            dkmsRuntimeOptions.setSocks5Proxy(runtimeOptions.socks5Proxy);
        }
        return dkmsRuntimeOptions;
    }
}
