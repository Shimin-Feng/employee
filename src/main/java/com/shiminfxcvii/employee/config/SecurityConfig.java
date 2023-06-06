package com.shiminfxcvii.employee.config;

import com.shiminfxcvii.employee.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;

/**
 * Spring Boot Security 用于用户的登录验证
 *
 * @author ShiminFXCVII
 * @since 2022/5/1 14:45
 */
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 密码明文加密方式配置
     *
     * @return 要使用的密码编码器
     * @author ShiminFXCVII
     * @since 2022/10/4 17:14
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用默认映射创建一个 DelegatingPasswordEncoder。 可能会添加其他映射，并且会更新编码以符合最佳实践。
        // 但是，由于 DelegatingPasswordEncoder 的性质，更新不应影响用户。 当前的映射是：BCryptPasswordEncoder
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 由基于提供的反应性请求提供 CorsConfiguration 实例的类（通常是 HTTP 请求处理程序）实现的接口。
     *
     * @return 接受过滤器使用的 CorsConfigurationSource 的构造函数，以查找要用于每个传入请求的 CorsConfiguration。
     * @author ShiminFXCVII
     * @since 2022/10/4 19:46
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // 用于 CORS 配置的容器以及用于检查给定请求的实际来源、HTTP 方法和标头的方法。
        // 默认情况下，新创建的 CorsConfiguration 不允许任何跨源请求，必须明确配置以指示应允许的内容。
        // 使用 applyPermitDefaultValues() 翻转初始化模型，以开放默认值开始，这些默认值允许 GET、HEAD 和 POST 请求的所有跨源请求。
        var config = new CorsConfiguration();
        // 设置允许的来源
        // 允许跨源请求的源列表。默认情况下未设置，这意味着不允许任何来源
        config.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
        // TODO 设置允许的原点模式
        // setAllowedOrigins 的替代方案，它支持更灵活的来源模式，除了端口列表之外，主机名中的任何位置都带有“*”。 例子：
        // https://*.domain1.com -- 以 domain1.com 结尾的域
        // https://*.domain1.com:[8080,8081] -- 在端口 8080 或端口 8081 上以 domain1.com 结尾的域
        // https://*.domain1.com:[*] -- 在任何端口上以 domain1.com 结尾的域，包括默认端口
        // 逗号分隔的模式列表，例如 "https://*.a1.com,https://*.a2.com";
        // 当通过属性占位符解析值时，这很方便，例如 "${原点}"; 请注意，此类占位符必须在外部解析。
        // 与仅支持“*”且不能与 allowCredentials 一起使用的 allowedOrigins 相比，当匹配 allowedOriginPattern 时，
        // Access-Control-Allow-Origin 响应标头将设置为匹配的来源，而不是“*”或模式。
        // 因此，allowedOriginPatterns 可以与设置为 true 的 setAllowCredentials 结合使用。
        // 默认情况下未设置。
//        config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:552*"));
        // 设置允许的方法
        // 将 HTTP 方法设置为允许，例如 “GET”、“POST”、“PUT”等。
        // 特殊值“*”允许所有方法。
        // 如果未设置，则只允许使用“GET”和“HEAD”。
        // 默认情况下未设置。
        config.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));
        // 设置允许的请求头
        // 将飞行前请求可以列出的标头列表设置为允许在实际请求期间使用。
        // 特殊值“*”允许实际请求发送任何标头。
        // 如果标头名称是以下之一，则不需要列出：Cache-Control、Content-Language、Expires、Last-Modified 或 Pragma。
        // 默认情况下未设置。
        config.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
        // TODO 设置暴露的请求头
        // 设置响应标头列表，而不是简单标头（即 Cache-Control、Content-Language、Content-Type、Expires、Last-Modified 或 Pragma）
        // 实际响应可能具有并且可以公开。
        // 特殊值“*”允许为非凭证请求公开所有标头。
        // 默认情况下未设置。
//        config.setExposedHeaders(Collections.singletonList(CorsConfiguration.ALL));
        // 设置允许凭据
        // 是否支持用户凭据。
        // 默认情况下未设置（即不支持用户凭据）。
        config.setAllowCredentials(true);
        // 设置最大有效期
        // 配置客户端可以缓存飞行前请求的响应多长时间（以秒为单位）。
        // 默认情况下未设置。
        config.setMaxAge(1800L);
        // CorsConfigurationSource 使用 URL 模式为请求选择 CorsConfiguration。
        var source = new UrlBasedCorsConfigurationSource();
        // 注册 Cors 配置
        // 为指定的路径模式注册一个 CorsConfiguration。
        // 添加映射路径，拦截一切请求
        source.registerCorsConfiguration("/**", config);
        // 接受过滤器使用的 CorsConfigurationSource 的构造函数，以查找要用于每个传入请求的 CorsConfiguration。
        return source;
    }

    /**
     * 对登录、退出、页面的访问权限、静态资源的管理
     *
     * @param http HttpSecurity
     * @throws Exception 未知异常
     * @author ShiminFXCVII
     * @since 2022/5/1 14:50
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CorsConfigurationSource corsConfigurationSource,
            UserDetailsService userDetailsService,
            PersistentTokenRepository persistentTokenRepository
    ) throws Exception {

        // 将安全标头添加到响应中。使用 EnableWebSecurity 时默认激活。接受 EnableWebSecurity 提供的默认值或仅调用 headers() 而不对其调用其他方法
//        http.headers()
//                // Adds a HeaderWriter instance
//                .addHeaderWriter(null)
//                // 配置插入 X-Content-Type-Options 的 XContentTypeOptionsHeaderWriter:
//                // X-Content-Type-Options: nosniff
//                .contentTypeOptions(null)
//                // 请注意，这不是全面的 XSS 保护！
//                // 允许自定义 XXssProtectionHeaderWriter 添加 X-XSS-Protection 标头
//                .xssProtection(null)
//                // 允许自定义 CacheControlHeadersWriter。 具体来说，它添加了以下标头：
//                // Cache-Control: no-cache, no-store, max-age=0, must-revalidate
//                // Pragma: no-cache
//                // Expires: 0
//                .cacheControl(null)
//                // 允许自定义为 HTTP 严格传输安全 (HSTS) 提供支持的 HstsHeaderWriter
//                .httpStrictTransportSecurity(null)
//                // 允许自定义 XFrameOptionsHeaderWriter。
//                .frameOptions(null)
//                // 允许自定义 HpkpHeaderWriter，它提供对 HTTP 公钥固定 (HPKP) 的支持
//                .httpPublicKeyPinning(null)
//                // 允许配置内容安全策略 (CSP) 级别 2。
//                // 调用此方法会使用提供的安全策略指令在响应中自动启用（包括）Content-Security-Policy 标头。
//                // 向 ContentSecurityPolicyHeaderWriter 提供了配置，它支持编写 W3C 候选建议中详述的两个标头：
//                // Content-Security-Policy
//                // Content-Security-Policy-Report-Only
//                .contentSecurityPolicy(contentSecurityPolicyConfig -> {
//                })
//                // 从响应中清除所有默认标头。 这样做之后，可以添加标头。 例如，如果你只想使用 Spring Security 的缓存控制，你可以使用以下内容：
//                // http.headers().defaultsDisabled().cacheControl();
//                .defaultsDisabled()
//                // 允许配置 Referrer Policy。
//                // 向 ReferrerPolicyHeaderWriter 提供了配置，它支持写入标头，如 W3C 技术报告中所述：
//                // Referrer-Policy
//                .referrerPolicy(referrerPolicyConfig -> {
//                })
//                // 允许配置 Cross-Origin-Opener-Policy 标头。
//                // 调用此方法会使用提供的策略在响应中自动启用（包括）Cross-Origin-Opener-Policy 标头。
//                // 配置提供给负责编写标头的 CrossOriginOpenerPolicyHeaderWriter。
//                .crossOriginOpenerPolicy(Customizer.withDefaults())
//                // 允许配置 Cross-Origin-Embedder-Policy 标头。
//                // 调用此方法会使用提供的策略在响应中自动启用（包括）Cross-Origin-Embedder-Policy 标头。
//                // 配置提供给负责编写标头的 CrossOriginEmbedderPolicyHeaderWriter。
//                .crossOriginEmbedderPolicy(crossOriginEmbedderPolicyConfig -> {
//                })
//                // 允许配置 Cross-Origin-Resource-Policy 标头。
//                // 调用此方法会使用提供的策略在响应中自动启用（包括）Cross-Origin-Resource-Policy 标头。
//                // 配置提供给负责编写标头的 CrossOriginResourcePolicyHeaderWriter：
//                .crossOriginResourcePolicy(crossOriginResourcePolicyConfig -> {
//                })
//                .disable();

        // 添加要使用的 CorsFilter。 如果提供了名为 corsFilter 的 bean，则使用该 CorsFilter。
        // 否则，如果定义了 corsConfigurationSource，则使用该 CorsConfiguration。
        // 否则，如果 Spring MVC 在类路径上，则使用 HandlerMappingIntrospector。
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource));

        http.csrf(AbstractHttpConfigurer::disable);

        return http
                // 关闭 csrf（Cross-site request forgery 跨站请求伪造）防护
                // 注释掉则开启防护
                /*.csrf()
                .disable()*/
                // 表单认证
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        // 设置登录页面
//                        .loginPage("/login")
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
                           有了下面这行代码，登录成功后地址栏才会显示正确的 `http://localhost:8080/index` 地址，
                           而不是 `http://localhost:8080/login`
                           并且如果是：.defaultSuccessUrl("/index")，这个时候会出现个问题，假如我们还没有登录认证，
                           在浏览器输入一个不存在的 url，
                           例如 localhost:8080/test，那么通过此配置，security 会帮我们导向登录页面，
                           然后当我们登录成功后你会发现跳转的路径变成了 /test，
                           而不是设置的 /index。使用 .defaultSuccessUrl("/index", true)，那么就不会出现上面问题，会直接转到 /index
                         */
//                        .defaultSuccessUrl("/index", true)
                        // 登录失败后的请求访问的地址，这里访问的是控制器
//                        .failureForwardUrl("/loginFailed")
                )
                // 所有的登录请求都被允许，不设置就无法访问登录界面

                // session 禁用配置
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 授权请求
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        // 不需要拦截的页面
                        .requestMatchers(
                                "/login",
                                "/loginFailed",
                                "/logout",

                                "/actuator/**",
                                "/error",
                                "/favicon.ico",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        )
                        .permitAll()
                        // 需要拦截的页面
                        // 如要访问 employee 页面必须具有 ROLE_ADMIN 权限
                        // ROLE_ADMIN 这个写法不对，这里不需要加 ROLE_ 前缀
                        // TODO: 下面这行为什么没有效果？
//                .antMatchers("/index").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/employee").hasAuthority("ADMIN")
//                .antMatchers("/user").hasAuthority("USER")
                        // 任何请求都需要通过认证
                        .anyRequest()
                        .authenticated())
                // 开启’记住我‘功能
                .rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer
                        // ’记住我‘ 86400 秒/一天，即使服务器重启也不会下线，除非其主动退出登录
                        .tokenValiditySeconds(86400)
                        // 并存储进数据库
                        .tokenRepository(persistentTokenRepository)
                        /*
                            不加下面这行代码 `有时候` 会报以下异常
                            POST http://localhost:8080/login 403
                            Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception
                            Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.
                            加了也会报错
                            如果登陆时勾选了````记住我````，那么在登录状态下重启服务器后再点击退出就会出现这个问题
                         */
                        .userDetailsService(userDetailsService))
                /*.and()
                // 已在 HTTPStatusCodeErrorController class 中处理
                // 开启异常处理
                .exceptionHandling()
                // 拒绝访问后给到的页面
                .accessDeniedPage("/403")*/
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        // 退出成功后访问的地址
                        .logoutSuccessUrl("/login"))
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
                .build()
                ;
    }

    /**
     * 将 remember me token 信息存储进数据库
     *
     * @return {@code tokenRepository} 由该 token 仓库创建一个 token 并返回
     * @author ShiminFXCVII
     * @see PersistentTokenRepository
     * @since 2022/5/1 15:15
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) throws SQLException {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        // 设置数据源
        tokenRepository.setDataSource(dataSource);
        // 如果表不存在则创建
        // 下面判断已替换掉 class JdbcCheckTableExit.java
        if (!DataSourceUtils.getConnection(dataSource)
                .getMetaData()
                .getTables(null, Constants.EMPLOYEE_MANAGEMENT, Constants.PERSISTENT_LOGINS, Constants.TABLE)
                .next()) {
            tokenRepository.setCreateTableOnStartup(true);
        }
        return tokenRepository;
    }

}