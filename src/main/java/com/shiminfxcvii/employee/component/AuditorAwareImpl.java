package com.shiminfxcvii.employee.component;

import com.shiminfxcvii.employee.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author ShiminFXCVII
 * @see
 * @since 2023/6/6 13:09
 */
@Component
public class AuditorAwareImpl implements AuditorAware<User> {
    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.of((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}