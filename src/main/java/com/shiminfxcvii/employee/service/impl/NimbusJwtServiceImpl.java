package com.shiminfxcvii.employee.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.shiminfxcvii.employee.service.NimbusJwtService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

/**
 * NimbusJwt 服务实现
 *
 * @author ShiminFXCVII
 * @since 2/20/2023 11:21 PM
 */
@Service
public class NimbusJwtServiceImpl implements NimbusJwtService {

    private static final Log logger = LogFactory.getLog(NimbusJwtServiceImpl.class);

    /**
     * 设置不早于 （nbf） 声明，该声明标识不得接受 JWT 进行处理的时间。
     */
    private static final Instant NOT_BEFORE;

    /**
     * JwtEncoder：spring security jose 基于 Nimbus 的 JWT 加密类
     */
    private static final NimbusJwtEncoder NIMBUS_JWT_ENCODER;

    /**
     * JwtDecoder 的低级 Nimbus 实现，采用原始 Nimbus 配置。
     */
    private static final NimbusJwtDecoder NIMBUS_JWT_DECODER;

    static {
        // 文件由 keytool 生成
        // 证书路径
        ClassPathResource crtClassPathResource = new ClassPathResource("/store/shiting-soil.crt");
        // 密钥库路径
        ClassPathResource keystoreClassPathResource = new ClassPathResource("/store/shiting-soil.keystore");
        Path crtPath;
        Path keystorePath;
        try (InputStream crtInputStream = crtClassPathResource.getInputStream();
             InputStream keystoreInputStream = keystoreClassPathResource.getInputStream()) {
            crtPath = Path.of("shiting-soil-" + UUID.randomUUID() + ".crt");
            keystorePath = Path.of("shiting-soil-" + UUID.randomUUID() + ".keystore");
            Files.copy(crtInputStream, crtPath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(keystoreInputStream, keystorePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File crtFile = crtPath.toFile();
        File keystoreFile = keystorePath.toFile();

        // 检查有效期
        X509Certificate cert;
        try (FileInputStream fis = new FileInputStream(crtFile)) {
            // 返回实现指定证书类型的证书工厂对象。
            // 此方法遍历已注册的安全提供程序列表，从最首选的提供程序开始。
            // 返回一个新的 CertificateFactory 对象，该对象封装了来自支持指定类型的第一个提供程序的 CertificateFactorySpi 实现。
            // 请注意，可以通过 Security.getProviders() 方法检索已注册提供程序的列表。
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // 生成证书对象，并使用从 InStream 中的输入流读取的数据对其进行初始化。
            cert = (X509Certificate) cf.generateCertificate(fis);
            // 检查证书当前是否有效。如果当前日期和时间在证书中给出的有效期限内。
            // 有效期由两个日期/时间值组成：证书生效的第一个和最后一个日期（以及时间）。
            cert.checkValidity();
        } catch (CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
        // 从证书的有效期中获取 notBefore 日期。
        NOT_BEFORE = cert.getNotBefore().toInstant();
        // 从证书的有效期中获取 notAfter 日期。
        // 设置过期时间 （exp） 声明，该声明标识不得接受 JWT 进行处理的时间或之后的时间。默认一周
        Instant notAfter = cert.getNotAfter().toInstant();
        logger.warn("RSA 密钥对和自签名证书开始时间: " + NOT_BEFORE.atZone(ZoneId.systemDefault()));
        logger.warn("RSA 密钥对和自签名证书截止时间: " + notAfter.atZone(ZoneId.systemDefault()));
        logger.warn("RSA 密钥对和自签名证书有效期剩: " + Duration.between(Instant.now(), notAfter).toDays() + " 天");

        // encode
        // keystore 密码
        char[] password = "mmv202005".toCharArray();
        RSAKey rsaKey;
        try {
            // 从指定的 JCA 密钥存储中加载公共/专用 RSA JWK。
            rsaKey = RSAKey.load(KeyStore.getInstance(keystoreFile, password), "shiting-soil-keystore", password);
        } catch (KeyStoreException | JOSEException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RuntimeException(e);
        }
        // 使用单个密钥创建新的 JSON Web 密钥 （JWK） 集。
        JWKSet jwkSet = new JWKSet(rsaKey);
        // 创建一个由不可变的 JWK 集支持的新 JWK 源。
        ImmutableJWKSet<SecurityContext> securityContextImmutableJWKSet = new ImmutableJWKSet<>(jwkSet);
        NIMBUS_JWT_ENCODER = new NimbusJwtEncoder(securityContextImmutableJWKSet);

        // decode
        // 不安全（纯）、签名和加密的 JSON Web 令牌 （JWT） 的默认处理器。
        DefaultJWTProcessor<SecurityContext> defaultJWTProcessor = new DefaultJWTProcessor<>();
        // 创建新的 JWS 验证密钥选择器。
        // 形参: jwsAlg – 允许要验证的对象 JWS 算法。不得为空。
        //      jwkSource – JWK source。不得为空。
        JWSKeySelector<SecurityContext> selector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, securityContextImmutableJWKSet);
        defaultJWTProcessor.setJWSKeySelector(selector);
        // 使用给定参数配置 NimbusJwtDecoder
        NIMBUS_JWT_DECODER = new NimbusJwtDecoder(defaultJWTProcessor);
        boolean delete = crtFile.delete();
        logger.warn("临时证书文件是否已删除: " + delete);
        delete = keystoreFile.delete();
        logger.warn("临时密钥库文件是否已删除: " + delete);
    }

    /**
     * 获取配置文件中设置的端口号
     */
    @Value("${server.port}")
    private int port;

    /**
     * 允许跨源请求的源列表
     */
    @Value("${security.cors.allowed-origins}")
    private List<String> allowedOrigins;

    /**
     * uri 方案，http 或者 https
     */
    @Value("${security.server.schema}")
    private String schema;

    /**
     * 服务器地址
     */
    @Value("${security.server.address}")
    private String address;

    /**
     * 根据用户 id 和用户名进行编码并返回生成的 JWT
     *
     * @param userId   用户 id，不能为空
     * @param username 用户名，不能为空
     * @return 生成的 JWT
     * @author ShiminFXCVII
     * @since 2/21/2023 12:06 PM
     */
    @Override
    public Jwt encode(Long userId, String username) {
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(username, "username cannot be null");
        return NIMBUS_JWT_ENCODER.encode(
                // 返回一个新的 JwtEncoderParameters，使用提供的 JwtClaimsSet 进行初始化。
                JwtEncoderParameters.from(
                        JwtClaimsSet.builder()
                                // 设置颁发者 （iss） 声明，该声明标识颁发 JWT 的主体。
                                // 形参: 颁发者 – 颁发者标识符
                                .issuer(schema + "://" + address + ":" + port)
                                // 设置主题（子）声明，该声明标识作为 JWT 主题的主体。
                                // 形参: 主题 – 主题标识符
                                .subject(userId.toString())
                                // 设置受众 （aud） 声明，该声明标识 JWT 所针对的收件人。
                                // 形参: 受众 – 此 JWT 所针对的受众
                                .audience(allowedOrigins)
                                // 设置过期时间 （exp） 声明，该声明标识不得接受 JWT 进行处理的时间或之后的时间。
                                // 形参: expiresAt – 不得接受 JWT 进行处理的时间或之后的时间
                                .expiresAt(Instant.now().plusSeconds(60 * 60 * 24 * 7))
                                // 设置不早于 （nbf） 声明，该声明标识不得接受 JWT 进行处理的时间。
                                // 形参: notBefore——不得接受 JWT 进行处理的时间
                                .notBefore(NOT_BEFORE)
                                // 设置颁发时间 （iat） 声明，该声明标识颁发 JWT 的时间。
                                // 形参: 发布时间 – JWT 发布的时间
                                .issuedAt(Instant.now())
                                // 设置 JWT ID （jti） 声明，该声明为 JWT 提供唯一标识符。
                                // 形参: JTI – JWT 的唯一标识符
                                .id(UUID.randomUUID().toString())
                                .claim(OAuth2ParameterNames.USERNAME, username)
                                .build()));
    }

    /**
     * 从其紧凑的声明表示格式解码和验证 JWT
     *
     * @param token JWT 值，不能为空
     * @return 经过验证的 JWT
     * @author ShiminFXCVII
     * @since 2/21/2023 12:54 PM
     */
    @Override
    public Jwt decode(String token) {
        Assert.notNull(token, "token cannot be null");
        return NIMBUS_JWT_DECODER.decode(token);
    }

}