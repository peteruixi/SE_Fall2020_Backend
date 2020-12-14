package com.healthMonitor.fall2020.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
Written by: Ruixi Li
Tested by: Ruixi Li
Debugged by: Ruixi Li
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.healthMonitor.fall2020.controller"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("SpringBoot X Swagger")
                        .description("SpringBoot X Swagger")
                        .version("9.0")
                        .contact(new Contact("Software Engineering Health Monitoring System Fall2020","",""))
                        .license("Fall2020")
                        .licenseUrl("")
                        .build());
    }
}

