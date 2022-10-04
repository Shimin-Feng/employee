//package com.shiminfxcvii.config;
//
//import org.assertj.core.util.Lists;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.bind.annotation.RestController;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.*;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.Collections;
//
///**
// * swagger
// *
// * @author shiminfxcvii
// * @since 2022/10/3 14:28 周一
// */
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
//    @Bean
//    public Docket createAdminRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .globalOperationParameters(
//                        Lists.newArrayList(
//                                new ParameterBuilder().parameterType("header")
//                                        .required(false)
//                                        .description("认证token")
//                                        .modelRef(new ModelRef("string"))
//                                        .defaultValue("Bearer eyJhbGciOiJIUzI1NiJ9.eyJwcmluY2lwYWwiOiIxNTcyNTAyODQzNjU3NTY4MjU3IiwiaXNzIjoiaHR0cHM6XC9cL3d3dy5jc2JhaWMuY29tXC8iLCJzdWIiOiJjc2JhaWMiLCJwcmluY2lwYWxfdHlwZSI6IiJ9.JPtjfTTgj4c_HD0bPKnoPsVX7ri9OMbpWTPfZkVwomI")
//                                        .name("Authorization")
//                                        .build()
//                        )
//                )
//                .securitySchemes(Collections.singletonList(securityScheme()))
//                .securityContexts(Collections.singletonList(securityContext()))
//                .host("localhost:9999")
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.csbaic.edatope"))
//                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    @Bean
//    SecurityScheme securityScheme() {
//        return new ApiKey("bearer", "Authorization", "header");
//    }
//
//    @Bean
//    SecurityContext securityContext() {
//        SecurityReference securityReference = SecurityReference.builder()
//                .reference("bearer")
//                .scopes(new AuthorizationScope[]{})
//                .build();
//
//        return SecurityContext.builder()
//                .securityReferences(Collections.singletonList(securityReference))
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("员工管理系统")
//                .description("员工管理系统接口文档")
//                .termsOfServiceUrl("")
//                .version("1.0")
//                .build();
//    }
//
//}
