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

    @Override
    protected void configure(@NotNull AuthenticationManagerBuilder auth) throws Exception {
        auth
                // passwordEncoder(new BCryptPasswordEncoder()) 密码加密方式
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
                // 关闭 csrf（Cross-site request forgery 跨站请求伪造）防护
                .csrf()
                .disable()
                .formLogin()
                // 所有的登录请求都被允许，不设置就无法访问登录界面
                .loginPage("/login")
                .permitAll()
                // 登录处理网址
                // TODO: 有何作用？
                /*.loginProcessingUrl("/login")
                // 登录成功后到达的页面
                .successForwardUrl("/employee/index")*/
                .and()
                // 授权请求
                // TODO: 验证出问题了！！！
                .authorizeRequests()
                // 如要访问 employee/* 页面必须具有 ROLE_ADMIN 权限
                .antMatchers("/employee/index").hasRole("ADMIN")
                .antMatchers("/user").hasAuthority("USER")
                // .antMatchers("/timeout").permitAll()
                // 任何请求都需要通过认证
                .anyRequest()
                .authenticated()
                /*.and()
                // 开启超时检测
                .sessionManagement()
                // 如果超时则跳转到以下页面
                .invalidSessionUrl("/timeout")*/
                /*.and()
                // 开启’记住我‘功能
                .rememberMe()
                // ’记住我‘ 86400 秒/一天，即使服务器重启也不会下线，除非其主动退出登录
                .tokenValiditySeconds(86400)
                // 并存储进数据库
                .tokenRepository(new TokenRepository().persistentTokenRepository())*/;

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
            return tokenRepository;
        }
    }
}