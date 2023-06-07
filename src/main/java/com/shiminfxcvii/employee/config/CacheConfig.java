package com.shiminfxcvii.employee.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置类
 *
 * @author ShiminFXCVII
 * @since 2023/6/7 21:51
 */
@Configuration
public class CacheConfig {

    /**
     * Spring 的中央缓存管理器 SPI。
     * 允许检索命名 Cache 区域。
     *
     * @return CacheManager 实现
     * @author ShiminFXCVII
     * @since 2023/4/14 22:03
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("UserCache", "CaptchaCache");
    }

}