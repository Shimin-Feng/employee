package com.shiminfxcvii.employee.filter;

import com.shiminfxcvii.employee.entity.User;
import com.shiminfxcvii.employee.properties.SecurityProperties;
import com.shiminfxcvii.employee.service.NimbusJwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * Jwt 请求调度的一次执行认证过滤器
 *
 * @author zhq
 * @author ShiminFXCVII
 * @since 11/22/2022 2:19 PM
 */
@Component
public class JwtOncePerRequestFilter extends OncePerRequestFilter {

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private final NimbusJwtService nimbusJwtService;
    private final SecurityProperties securityProperties;
    private final CacheManager cacheManager;

    public JwtOncePerRequestFilter(
            NimbusJwtService nimbusJwtService,
            SecurityProperties securityProperties,
            CacheManager cacheManager
    ) {
        this.nimbusJwtService = nimbusJwtService;
        this.securityProperties = securityProperties;
        this.cacheManager = cacheManager;
    }

    /**
     * 与 doFilter 的契约相同，但保证在单个请求线程中每个请求只调用一次。有关详细信息，请参阅 {@link #shouldNotFilterAsyncDispatch()}。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 判断本次请求是否需要拦截
        for (String path : securityProperties.getWhitelist()) {
            if (ANT_PATH_MATCHER.match(path, request.getRequestURI())) {
                filterChain.doFilter(request, response);
                // return 是必须的
                return;
            }
        }
        // 校验请求是否携带 token
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
        // 缓存用户信息到 SecurityContext
        Object username = jwt.getClaim(OAuth2ParameterNames.USERNAME);
        User user = Objects.requireNonNull(cacheManager.getCache("UserCache")).get(username, User.class);
        if (user == null || user.getUserDetails() == null) {
            throw new AuthenticationServiceException("无法获取到用户信息");
        }
        UserDetails userDetails = user.getUserDetails();
        var token = UsernamePasswordAuthenticationToken.authenticated(user, userDetails.getPassword(), userDetails.getAuthorities());
        token.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }

}