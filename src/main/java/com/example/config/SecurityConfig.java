package com.example.config;

import com.example.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
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
    private UserService userService;
    @Resource
    private DataSource dataSource;

    @Override
    protected void configure(@NotNull AuthenticationManagerBuilder auth) throws Exception {
        auth
                // passwordEncoder(new BCryptPasswordEncoder()) 密码加密方式
                .userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());

//                // 把账号密码存储在内存中的方法
//                .inMemoryAuthentication()
//                .passwordEncoder(new BCryptPasswordEncoder())
//                // 在登录成功后不退出就直接登录其他账号是会被挤下线的
//                .withUser("admin")
//                .password(new BCryptPasswordEncoder().encode("hKJB$$%8ffFpGnLWE"))
//                // 比 hasRole() 更加通用
//                .authorities("ADMIN");
    }


    // TOTO: 后续可以添加用户管理界面，管理请假界面
    @Override
    protected void configure(@NotNull HttpSecurity http) throws Exception {
        http
                // 关闭 csrf（Cross-site request forgery 跨站请求伪造）防护
                // 注释掉则开启防护
                /*.csrf()
                .disable()*/
                // 表单认证
                .formLogin()
                // 设置登录页面
                .loginPage("/login")
                /*
                   登录处理网址
                   当收到 /login 请求时会认为是登录，需要执行我们自定义的登录逻辑，里面的 url 是登录页面表单提交地址
                   在本项目现在的状态下可以不写！！！
                   如果值改成 "/index"，就会一直在登录界面
                */
                .loginProcessingUrl("/login")
                // 登录成功后的请求访问的地址，请求方法必须是 post，这里是跳转控制器
                // 下面这行代码似乎并没有任何作用，URL 随便写都没有影响
//                .successForwardUrl("/khbgkhjvkjh")
                /*
                   有了下面这行代码，登录成功后地址栏才会显示正确的 `http://localhost:8080/index` 地址，而不是 `http://localhost:8080/login`
                   并且如果是：.defaultSuccessUrl("/index")，这个时候会出现个问题，假如我们还没有登录认证，在浏览器输入一个不存在的 url，
                   例如 localhost:8080/test，那么通过此配置，security 会帮我们导向登录页面，然后当我们登录成功后你会发现跳转的路径变成了 /test，
                   而不是设置的 /index。使用 .defaultSuccessUrl("/index", true)，那么就不会出现上面问题，会直接转到 /index
                 */
                .defaultSuccessUrl("/index", true)
                // 登录失败后的请求访问的地址，这里访问的是控制器
                .failureForwardUrl("/loginFailed")
                // 所有的登录请求都被允许，不设置就无法访问登录界面
                .and()
                // 授权请求
                .authorizeRequests()
                // 不需要拦截的页面
                .antMatchers(/*"/403", */"/login", "/loginFailed", "/logout"/*, "/timeout"*/).permitAll()
                // 需要拦截的页面
                // 如要访问 employee 页面必须具有 ROLE_ADMIN 权限
                // ROLE_ADMIN 这个写法不对，这里不需要加 ROLE_ 前缀
                // TODO: 下面这行为什么没有效果？
//                .antMatchers("/index").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/employee").hasAuthority("ADMIN")
//                .antMatchers("/user").hasAuthority("USER")
                // 任何请求都需要通过认证
                .anyRequest()
                .authenticated()
                .and()
                // 开启’记住我‘功能
                .rememberMe()
                // ’记住我‘ 86400 秒/一天，即使服务器重启也不会下线，除非其主动退出登录
                .tokenValiditySeconds(86400)
                // 并存储进数据库
                .tokenRepository(persistentTokenRepository())
                /*.and()
                // 已在 HTTPStatusCodeErrorController class 中处理
                // 开启异常处理
                .exceptionHandling()
                // 拒绝访问后给到的页面
                .accessDeniedPage("/403")*/
                .and()
                .logout()
                // 退出成功后访问的地址
                .logoutSuccessUrl("/login")
                /*.and()
                // 已在 HTTPStatusCodeErrorController class 中处理
                // 暂时注释掉，因为不好控制
                // TODO: 怎么才能控制好 timeout？
                // 似乎正常了，然而我并没有改变什么
                // 开启超时检测
                .sessionManagement()
                // 如果超时则跳转到以下页面
                .invalidSessionUrl("/timeout")
                // 设置最大 Session 数为 1
                .maximumSessions(1)*/
        ;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        // 反射尝试失败
        // Class<JdbcTokenRepositoryImpl> jdbcTokenRepositoryClass = JdbcTokenRepositoryImpl.class;
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        // 设置数据源
        tokenRepository.setDataSource(dataSource);
        // 如果表不存在则创建
        if (!JdbcCheckTableExit.isExist()) {
            tokenRepository.setCreateTableOnStartup(true);
        }
        return tokenRepository;
    }
}