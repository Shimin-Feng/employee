package com.shiminfxcvii.service;

import com.shiminfxcvii.entity.User;
import com.shiminfxcvii.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @class UserService
 * @created 2022/5/1 15:31
 * @description 该类用于验证在有账号登录时是否与数据库账号匹配
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    /**
     * TODO<br>
     * 登录时勾选了 remember me —— 不退出 —— 重启服务器 —— 再点击退出 —— 就会报 403 错误<br>
     * 后台没有错误报告，前台和页面会报错 POST <a href="http://localhost:8080/logout">http://localhost:8080/logout</a> 403<br>
     * <br>
     * TODO<br>
     * 有时候第一次点击登录只是刷新了一下页面，第二次点击才会登录成功
     * <br>
     * TODO<br>
     * 有时会报错
     * Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception
     * Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();
        user.setUsername(username);
        return userRepository.findOne(Example.of(user)).map(value -> org.springframework.security.core.userdetails.User
                .withUsername(value.getUsername()).password(value.getPassword()).authorities(value.getAuthorities()
                        .split(",")).build()).orElse(null);
    }
}