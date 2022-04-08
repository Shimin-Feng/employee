package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * &#064;ClassName UserService
 * &#064;Author $himin F
 * &#064;Date
 * &#064;Version 1.0
 * &#064;description: 该类用于验证在有账号登录时是否与数据库账号匹配
 */

@Service
public class UserService implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    /**
     * 有时候会报以下错误：
     * An internal error occurred while trying to authenticate the user.
     * org.springframework.security.authentication.InternalAuthenticationServiceException:
     * UserDetailsService returned null, which is an interface contract violation
     * 已解决!在提交登录信息时在前台验证是否有数据
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();
        user.setUsername(username);
        Optional<User> optional = userRepository.findOne(Example.of(user));
        return optional.map(value -> org.springframework.security.core.userdetails.User.withUsername(value.getUsername())
                .password(value.getPassword()).authorities(value.getAuthorities().split(",")).build()).orElse(null);
    }
}