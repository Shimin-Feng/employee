package com.shiminfxcvii.employee.component;

import com.shiminfxcvii.employee.service.NimbusJwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 在 LogoutFilter 成功注销后调用的策略，用于处理重定向或转发到相应目标。
 * 请注意，该接口与 LogoutHandler 几乎相同，但可能会引发异常。LogoutHandler 实现希望被调用以执行必要地清理，因此不应引发异常。
 *
 * @author ShiminFXCVII
 * @since 12/27/2022 4:20 PM
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private final NimbusJwtService nimbusJwtService;
    private final CacheManager cacheManager;

    public LogoutSuccessHandlerImpl(NimbusJwtService nimbusJwtService, CacheManager cacheManager) {
        this.nimbusJwtService = nimbusJwtService;
        this.cacheManager = cacheManager;
    }

    /**
     * 自定义用户退出登录后的一些操作
     *
     * @param request        the request.
     * @param response       the response.
     * @param authentication 认证信息，始终为 null
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorization)) {
            throw new AuthenticationServiceException("令牌缺失");
        }
        String[] split = authorization.split(org.apache.commons.lang3.StringUtils.SPACE);
        if (split.length != 2) {
            throw new AuthenticationServiceException("非法令牌，令牌必须以 Bearer 为前缀并以一个空格分开");
        }
        if (!OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(split[0])) {
            throw new AuthenticationServiceException("非法令牌，令牌必须以 Bearer 为前缀并以一个空格分开");
        }
        Jwt jwt = nimbusJwtService.decode(split[1]);
        if (jwt == null) {
            throw new AuthenticationServiceException("令牌已过期或验证不正确");
        }
        Object username = jwt.getClaim(OAuth2ParameterNames.USERNAME);
        // 清除缓存的用户信息
        Objects.requireNonNull(cacheManager.getCache("UserCache")).evictIfPresent(username);
    }

}