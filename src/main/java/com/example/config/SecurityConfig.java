package com.example.config;

import com.example.repository.MyPasswordEncoder;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(@NotNull AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .passwordEncoder(new MyPasswordEncoder())
                // 在登录成功后不退出就直接登录其他账号是会被挤下线的
                .withUser("admin")
                .password(new MyPasswordEncoder().encode("hKJB$$%8ffFpGnLWE"))
                // 比 hasRole() 更加通用
                .authorities("ADMIN");
    }


    // 后续可以添加用户管理界面
    @Override
    protected void configure(@NotNull HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                // /employee/* 表示 employee 目录下所有的子目录
                .antMatchers("/employee/*", "/**/*.css", "/**/*.js")
                // 比 hasRole() 更加通用
                .hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
}