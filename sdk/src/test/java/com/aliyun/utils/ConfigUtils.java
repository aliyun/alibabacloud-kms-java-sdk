package com.aliyun.utils;

import com.aliyun.tea.utils.StringUtils;

import java.io.IOException;
import java.util.Properties;

public class ConfigUtils {
    public static Properties loadParam(String fileName) throws IOException {
        fileName = StringUtils.isEmpty(fileName) ? "test_param.properties" : fileName;
        Properties properties = new Properties();
        properties.load(ConfigUtils.class.getClassLoader().getResourceAsStream(fileName));
        return properties;
    }
}
