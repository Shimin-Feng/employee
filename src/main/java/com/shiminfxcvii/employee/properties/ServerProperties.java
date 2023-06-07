package com.shiminfxcvii.employee.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * server 配置
 *
 * @author ShiminFXCVII
 * @since 3/4/2023 3:25 PM
 */
@Configuration
@ConfigurationProperties(prefix = "security.server")
public class ServerProperties {
    /**
     * uri 方案，http 或者 https
     */
    private String schema = "";
    /**
     * 服务器地址
     */
    private String address = "";

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}