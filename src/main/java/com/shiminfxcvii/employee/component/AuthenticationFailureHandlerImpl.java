package com.shiminfxcvii.employee.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 用于处理失败的身份验证尝试的策略。
 * 典型的行为可能是将用户重定向到身份验证页面（在表单登录的情况下）以允许他们重试。根据异常的类型，可能会实现更复杂的逻辑。
 * 例如，CredentialsExpireException 可能会导致重定向到 Web 控制器，从而允许用户更改其密码。
 *
 * @author ShiminFXCVII
 * @since 12/22/2022 8:46 PM
 */
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public AuthenticationFailureHandlerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 尝试身份验证失败时调用
     *
     * @param request   发生于尝试身份验证的请求
     * @param response  the response.
     * @param exception 为拒绝身份验证请求而引发的异常
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(objectMapper.writeValueAsString(ResponseEntity.ok(exception.getMessage())));
    }

}