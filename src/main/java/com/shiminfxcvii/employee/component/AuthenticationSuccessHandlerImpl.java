package com.shiminfxcvii.employee.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 用于处理成功的用户身份验证的策略。
 * 实现可以做任何他们想做的事情，但典型的行为是控制到后续目的地的导航（使用重定向或转发）。
 * 例如，在用户通过提交登录表单登录后，应用程序需要决定之后应将其重定向到何处（请参阅 {@link AbstractAuthenticationProcessingFilter} 和子类）。
 * 如果需要，还可以包括其他逻辑。
 *
 * @author ShiminFXCVII
 * @since 12/22/2022 8:41 PM
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    public AuthenticationSuccessHandlerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     *                       the authentication process.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter()
                .write(objectMapper.writeValueAsString(
                        ResponseEntity.ok(Map.of(OAuth2ParameterNames.TOKEN, request.getAttribute(OAuth2ParameterNames.TOKEN)))
                ));
        request.removeAttribute(OAuth2ParameterNames.TOKEN);
    }

}