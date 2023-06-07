package com.shiminfxcvii.employee.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 放行白名单配置
 *
 * @author ShiminFXCVII
 * @since 3/4/2023 12:57 AM
 */
@Configuration
@ConfigurationProperties(prefix = "security.ignore")
public class IgnoreProperties {
    /**
     * 放行白名单配置，网关不校验此处的白名单
     */
    private String[] whites = new String[]{};

    public String[] getWhites() {
        return whites;
    }

    public void setWhites(String[] whites) {
        this.whites = whites;
    }
}