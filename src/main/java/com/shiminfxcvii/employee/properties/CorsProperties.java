package com.shiminfxcvii.employee.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * cors 配置
 *
 * @author ShiminFXCVII
 * @since 3/4/2023 1:36 PM
 */
@Configuration
@ConfigurationProperties(prefix = "security.cors")
public class CorsProperties {
    /**
     * 允许跨源请求的源列表
     */
    private List<String> allowedOrigins = new ArrayList<>();

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}