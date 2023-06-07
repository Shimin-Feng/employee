package com.shiminfxcvii.employee.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Base implementation of {@link AccessDeniedHandler}.
 * <p>
 * This implementation sends a 403 (SC_FORBIDDEN) HTTP error code. In addition, if an
 * {@link #errorPage} is defined, the implementation will perform a request dispatcher
 * "forward" to the specified error page view. Being a "forward", the
 * <code>SecurityContextHolder</code> will remain populated. This is of benefit if the
 * view (or a tag library or macro) wishes to access the
 * <code>SecurityContextHolder</code>. The request scope will also be populated with the
 * exception itself, available from the key {@link WebAttributes#ACCESS_DENIED_403}.
 *
 * @author Ben Alex
 */
@Component
public class CustomAccessDeniedHandlerImpl implements AccessDeniedHandler {

    private static final Log logger = LogFactory.getLog(CustomAccessDeniedHandlerImpl.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        logger.debug("Responding with 403 status code");
        response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
    }

}