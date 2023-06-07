package com.shiminfxcvii.employee.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 由 ExceptionTranslationFilter 用于启动身份验证方案。
 *
 * @author ShiminFXCVII
 * @since 3/9/2023 5:13 PM
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public AuthenticationEntryPointImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 启动身份验证方案。
     * 在调用此方法之前，ExceptionTranslationFilter 将使用请求的目标 URL 填充名为
     * AbstractAuthenticationProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY 的 HttpSession 属性。
     * 实现应根据需要修改 ServletResponse 上的标头以开始身份验证过程。
     *
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        String massage = null;
        // 如果身份验证请求因账户已过期而被拒绝，则引发。不断言凭据是否有效。
        if (authException instanceof AccountExpiredException) {
            massage = "账户已过期";
        }
        // 如果身份验证请求因账户凭据已过期而被拒绝，则引发。不断言凭据是否有效。
        else if (authException instanceof CredentialsExpiredException) {
            massage = "凭据已过期";
        }
        // 如果身份验证请求因账户被禁用而被拒绝，则引发。不断言凭据是否有效。
        else if (authException instanceof DisabledException) {
            massage = "账户已禁用";
        }
        // 如果身份验证请求因账户被锁定而被拒绝，则引发。不断言凭据是否有效。
        else if (authException instanceof LockedException) {
            massage = "账户已锁定";
        }
        // 由特定用户账户状态（锁定、禁用等）引起的身份验证异常的基类。
        else if (authException instanceof AccountStatusException) {
            massage = "账号状态身份验证异常";
        }

        // 如果由于安全上下文中没有身份验证对象而拒绝身份验证请求，则引发。
        else if (authException instanceof AuthenticationCredentialsNotFoundException) {
            massage = "未找到身份验证凭据";
        }

        // 如果由于内部发生的系统问题而无法处理身份验证请求，则抛出。 它与 AuthenticationServiceException 的不同之处在于，
        // 如果外部系统出现内部错误或故障，则不会抛出它。 这确保我们可以处理我们控制范围内的错误，区别于其他系统的错误。
        // 这种区别的好处是不受信任的外部系统不应该能够填满日志并导致过多的 IO。 但是，内部系统应该报告错误。
        // 例如，如果后端身份验证存储库不可用，则可能会抛出此错误。 但是，如果在验证来自 OIDC 提供者的 OIDC 响应时发生错误，则不会抛出它。
        else if (authException instanceof InternalAuthenticationServiceException) {
            massage = "内部身份验证服务异常";
        }
        // 如果由于系统问题而无法处理身份验证请求，则引发。例如，如果后端身份验证存储库不可用，则可能会引发此问题。
        else if (authException instanceof AuthenticationServiceException) {
            massage = "身份验证服务异常";
        }

        // 如果身份验证请求因凭据无效而被拒绝，则引发。对于要引发的异常，这意味着账户既未锁定也未禁用。
        else if (authException instanceof BadCredentialsException) {
            massage = "凭据无效，用户名或密码有误";
        }

        // Cookie 盗窃异常
        else if (authException instanceof CookieTheftException) {
            massage = "Cookie 盗窃异常";
        }
        // 无效的 Cookie 异常
        else if (authException instanceof InvalidCookieException) {
            massage = "无效的 Cookie 异常";
        }
        // 当使用 remember-me 身份验证时发生 org.springframework.security.core.Authentication 异常时抛出此异常。
        else if (authException instanceof RememberMeAuthenticationException) {
            massage = "记住我身份验证异常";
        }

        // 如果身份验证请求因凭据不够可信而被拒绝，则抛出该异常。
        // 如果 org.springframework.security.access.AccessDecisionVoters 对身份验证级别不满意，例如使用记住我机制或匿名执行，
        // 他们通常会抛出此异常。 然后，ExceptionTranslationFilter 通常会导致调用 AuthenticationEntryPoint，
        // 从而允许委托人使用更强级别的身份验证进行身份验证。
        else if (authException instanceof InsufficientAuthenticationException) {
            massage = "凭据不够可信";
        }

        // 对于所有与 OAuth 2.0 相关的身份验证错误，都会抛出此异常。
        // 有多种情况可能会发生错误，例如：
        // 授权请求或令牌请求缺少必需的参数
        // 客户端标识符缺失或无效
        // 无效或不匹配的重定向 URI
        // 请求的范围无效、未知或格式错误
        // 资源所有者或授权服务器拒绝了访问请求
        // 客户端认证失败
        // 提供的授权授予（授权代码、资源所有者凭据）无效、过期或已撤销
        else if (authException instanceof OAuth2AuthenticationException) {
            massage = "OAuth 2.0 相关的身份验证错误";
        }

        // 如果身份验证请求因摘要随机数已过期而被拒绝，则抛出该异常。
        else if (authException instanceof NonceExpiredException) {
            massage = "摘要随机数已过期";
        }

        // 未找到预验证凭据异常
        else if (authException instanceof PreAuthenticatedCredentialsNotFoundException) {
            massage = "未找到预验证凭据异常";
        }

        // 如果找不到支持提供的 org.springframework.security.core.Authentication 对象的 AuthenticationProvider，
        // 则由 ProviderManager 抛出。
        else if (authException instanceof ProviderNotFoundException) {
            massage = "未找到身份验证提供者";
        }

        // 由 SessionAuthenticationStrategy 抛出以指示身份验证对象对当前会话无效，通常是因为同一用户已超过允许他们同时拥有的会话数。
        else if (authException instanceof SessionAuthenticationException) {
            massage = "未找到身份验证提供者";
        }

        // 如果 UserDetailsService 实现无法通过用户名找到用户，则抛出该异常。
        else if (authException instanceof UsernameNotFoundException) {
            massage = "无法通过用户名找到用户";
        }

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(ResponseEntity.ok(massage)));
    }

}