package com.gavinjin.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
@Profile({"dev", "test"})
public class SwaggerConfig {
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // Must set the location of the controllers
                .apis(RequestHandlerSelectors.basePackage("com.gavinjin.backend.controller"))
                // If in production mode, do not expose all apis: DO NOT USE any()
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("User Management System")
                .description("Api documentation for the User Management System")
                .termsOfServiceUrl("https://github.com/GavinJin0501")
                .contact(new Contact("Gavin Jin", "https://github.com/GavinJin0501", "jiayao_jin@brown.edu"))
                .version("1.0")
                .build();
    }
}
