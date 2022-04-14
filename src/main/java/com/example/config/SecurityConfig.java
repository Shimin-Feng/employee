package com.example.config;

import com.example.service.UserService;
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
//                .passwordEncoder(new MyPasswordEncoder())
//                // 在登录成功后不退出就直接登录其他账号是会被挤下线的
//                .withUser("admin")
//                .password(new MyPasswordEncoder().encode("hKJB$$%8ffFpGnLWE"))
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
                // 登录处理网址
                // 当发现 login 时认为是登录需要执行我们自定义的登录逻辑，里面的 url 是登录页面表单提交地址
                // 在本项目现在的状态下可以不写！！！
                .loginProcessingUrl("/login")
                // 登录成功后的请求访问的地址，请求方法必须是 post，这里是跳转控制器
                .successForwardUrl("/index")
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
                // 已在 HTTPStatusCodeErrorController class 中处理
                // 暂时注释掉，因为不好控制
                // TODO: 怎么才能控制好 timeout？
                // 似乎正常了，然而我并没有改变什么
                // 开启超时检测
                .sessionManagement()
                // 如果超时则跳转到以下页面
                .invalidSessionUrl("/timeout")
                // 设置最大Session数为1
//                .maximumSessions(1)
                .and()
                // 开启’记住我‘功能
                .rememberMe()
                // ’记住我‘ 86400 秒/一天，即使服务器重启也不会下线，除非其主动退出登录
                .tokenValiditySeconds(86400)
                // 并存储进数据库
                .tokenRepository(new TokenRepository().persistentTokenRepository())
                /*.and()
                // 已在 HTTPStatusCodeErrorController class 中处理
                // 开启异常处理
                .exceptionHandling()
                // 拒绝访问后给到的页面
                .accessDeniedPage("/403")*/
                .and()
                .logout()
                // 退出成功后访问的地址
                .logoutSuccessUrl("/logout")
        ;
    }

    public class TokenRepository extends JdbcTokenRepositoryImpl {
        public PersistentTokenRepository persistentTokenRepository() {
            TokenRepository tokenRepository = new TokenRepository();
            JdbcCheckTableExitDemo jdbcCheckTableExitDemo = new JdbcCheckTableExitDemo();
            // 判断表是否存在，不存在则创建
            System.out.println("Token 表 persistent_logins 是否存在 --------------> " + jdbcCheckTableExitDemo.isExist());
            if (!jdbcCheckTableExitDemo.isExist()) {
                tokenRepository.setCreateTableOnStartup(true);
                tokenRepository.initDao();
            }
            tokenRepository.setDataSource(dataSource);
            return tokenRepository;
        }
    }
}