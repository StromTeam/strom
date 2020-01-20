package com.feng.strom.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${swagger.show:false}")
    private boolean swaggerShow;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerShow)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.feng.strom.web.controller"))
                .paths(PathSelectors.any())
                .build()
//                .globalOperationParameters(setHeaderToken())
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ISD 后台服务API接口文档")
                .description("API 描述")
                .contact(new Contact("ISD", "", ""))
                .version("1.0")
                .build();
    }

    private List<Parameter> setHeaderToken() {
        List<Parameter> pars = new ArrayList<>();

        // token请求头
//        String testTokenValue = "";
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        Parameter tokenParameter = tokenPar
//                .name(JwtTokenUtil.getTokenName())
//                .description("Token Request Header")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .defaultValue(testTokenValue)
//                .build();
//        pars.add(tokenParameter);
        return pars;
    }
}
