package com.aliyun.kms.kms20160120.utils;

import com.aliyun.tea.TeaException;

import java.util.HashMap;
import java.util.Map;

public class KmsErrorCodeTransferUtils {
    private static final Map<String, String> errorCodeMap = new HashMap<String, String>();
    public static final String INVALID_PARAM_ERROR_CODE = "InvalidParam";
    public static final String UNAUTHORIZED_ERROR_CODE = "Unauthorized";
    public static final String MISSING_PARAMETER_ERROR_CODE = "MissingParameter";
    public static final String INVALID_PARAMETER_ERROR_CODE = "InvalidParameter";
    public static final String FORBIDDEN_KEY_NOT_FOUND_ERROR_CODE = "Forbidden.KeyNotFound";
    public static final String INVALID_PARAMETER_KEY_SPEC_ERROR_MESSAGE = "The specified parameter KeySpec is not valid.";
    public static final String INVALID_PARAM_DATE_ERROR_MESSAGE = "The Param Date is invalid.";
    public static final String INVALID_PARAM_AUTHORIZATION_ERROR_MESSAGE = "The Param Authorization is invalid.";

    static {
        errorCodeMap.put(FORBIDDEN_KEY_NOT_FOUND_ERROR_CODE, "The specified Key is not found.");
        errorCodeMap.put("Forbidden.NoPermission", "This operation is forbidden by permission system.");
        errorCodeMap.put("InternalFailure", "Internal Failure");
        errorCodeMap.put("Rejected.Throttling", "QPS Limit Exceeded");
    }

    public static String transferErrorMessage(String errorCode) {
        return errorCodeMap.get(errorCode);
    }

    public static void transferInvalidDateException(TeaException e) {
        e.setCode("IllegalTimestamp");
        e.setMessage("The input parameter \"Timestamp\" that is mandatory for processing this request is not supplied.");
        Map<String, Object> data = e.getData();
        if (data != null && data.size() > 0) {
            data.put("Code", e.getCode());
            data.put("Message", e.getMessage());
        }
    }

    public static void transferInvalidAccessKeyIdException(TeaException e) {
        e.setCode("InvalidAccessKeyId.NotFound");
        e.setMessage("The input parameter \"Timestamp\" that is mandatory for processing this request is not supplied.");
        Map<String, Object> data = e.getData();
        if (data != null && data.size() > 0) {
            data.put("Code", e.getCode());
            data.put("Message", e.getMessage());
        }
    }

    public static void transferIncompleteSignatureException(TeaException e) {
        e.setCode("IncompleteSignature");
        e.setMessage("The request signature does not conform to Aliyun standards.");
        Map<String, Object> data = e.getData();
        if (data != null && data.size() > 0) {
            data.put("Code", e.getCode());
            data.put("Message", e.getMessage());
        }
    }
}
