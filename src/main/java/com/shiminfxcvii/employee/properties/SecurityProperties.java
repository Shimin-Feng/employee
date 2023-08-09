package com.shiminfxcvii.employee.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 放行白名单配置
 *
 * @author ShiminFXCVII
 * @since 3/4/2023 12:57 AM
 */
@Getter
@Configuration
@ConfigurationProperties(prefix = "security")
@Setter
public class SecurityProperties {
    /**
     * 服务器网络地址
     */
    private String address;
    /**
     * jwt 有效期时长，单位秒
     */
    private long expires;
    /**
     * 放行白名单配置，网关不校验此处的白名单
     */
    private String[] whitelist;
}