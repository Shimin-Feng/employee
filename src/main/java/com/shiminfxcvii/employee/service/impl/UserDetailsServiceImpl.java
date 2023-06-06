package com.shiminfxcvii.employee.service.impl;

import com.shiminfxcvii.employee.entity.User;
import com.shiminfxcvii.employee.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 该类用于验证在有账号登录时是否与数据库账号匹配
 *
 * @author ShiminFXCVII
 * @since 2022/5/1 15:31
 */

@Service
public final class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * TODO<br>
     * 登录时勾选了 remember me —— 不退出 —— 重启服务器 —— 再点击退出 —— 就会报 403 错误<br>
     * 后台没有错误报告，前台和页面会报错 POST <a href="http://localhost:8080/logout">http://localhost:8080/logout</a> 403<br>
     * <br>
     * TODO<br>
     * 有时候第一次点击登录只是刷新了一下页面，第二次点击才会登录成功<br>
     * <br>
     * TODO<br>
     * 有时会报错
     * Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception
     * Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.
     */
    @Override
    public UserDetails loadUserByUsername(@NotNull("用户名不能为空") String username) throws UsernameNotFoundException {
        return userRepository.findOne(Example.of(new User().setUsername(username))).map(user -> {
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getAuthorities().split(","))
                    .build();
            var token = UsernamePasswordAuthenticationToken.authenticated(user, user.getPassword(), userDetails.getAuthorities());
            token.setDetails(userDetails);
            SecurityContextHolder.getContext().setAuthentication(token);
            return userDetails;
        }).orElse(null);
    }

}