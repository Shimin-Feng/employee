package com.example.config;

import com.example.service.AccountService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AccountService accountService;
    @Resource
    private DataSource dataSource;

//    public SecurityConfig(AccountService accountService, DataSource dataSource) {
//        this.accountService = accountService;
//        this.dataSource = dataSource;
//    }

    @Override
    protected void configure(@NotNull AuthenticationManagerBuilder auth) throws Exception {
        auth
                // 把账号储存在数据库中，并对密码进行加密
                .userDetailsService(accountService).passwordEncoder(new BCryptPasswordEncoder());

//                .inMemoryAuthentication()
//                .passwordEncoder(new MyPasswordEncoder())
//                // 在登录成功后不退出就直接登录其他账号是会被挤下线的
//                .withUser("admin")
//                .password(new MyPasswordEncoder().encode("hKJB$$%8ffFpGnLWE"))
//                // 比 hasRole() 更加通用
//                .authorities("ADMIN");
    }


    // 后续可以添加用户管理界面
    @Override
    protected void configure(@NotNull HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .loginProcessingUrl("/login")
                .successForwardUrl("/employee/index")
                .and()
                .authorizeRequests()
                // 要访问 employee/index 员工页面必须具有 ADMIN 角色
                .antMatchers("/employee/index").hasRole("ADMIN")
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/timeout").permitAll()
                .anyRequest()
                .authenticated()
//                .and()
//                .sessionManagement()
//                .invalidSessionUrl("/timeout")
                .and()
                .rememberMe()
                .tokenValiditySeconds(2000)
                .tokenRepository(new TokenRepository().persistentTokenRepository());

//                .csrf()
//                .disable()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .authorizeRequests()
//                // /employee/* 表示 employee 目录下所有的子目录
//                .antMatchers("/employee/*", "/**/*.css", "/**/*.js")
//                // 比 hasRole() 更加通用
//                .hasAuthority("ADMIN")
//                .anyRequest()
//                .authenticated()
//                .and()
//                .and()
//                .logout()
//                .permitAll();
    }

    public class TokenRepository extends JdbcTokenRepositoryImpl {
        public PersistentTokenRepository persistentTokenRepository() {
            TokenRepository tokenRepository = new TokenRepository();
            JdbcCheckTableExitDemo jdbcCheckTableExitDemo = new JdbcCheckTableExitDemo();
            // 判断表是否存在，不存在则创建
            System.out.println("Token 表是否存在-------------------------------------------------" + jdbcCheckTableExitDemo.isExist());
            if (!jdbcCheckTableExitDemo.isExist()) {
                tokenRepository.setCreateTableOnStartup(true);
                tokenRepository.initDao();
            }
            tokenRepository.setDataSource(dataSource);
            System.out.println("已添加 Token 登录信息-------------------------------------------------");
            return tokenRepository;
        }
    }
}