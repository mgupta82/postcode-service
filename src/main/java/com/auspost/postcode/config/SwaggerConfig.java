package com.auspost.postcode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket postcodeApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.auspost.postcode.controller"))
                .paths(PathSelectors.regex("/postcode.*"))
                .build()
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfo("Postcode API"
        ,"REST API for Postcode"
        ,"1.0"
        ,"Terms of service"
        ,new Contact("Mukesh Gupta","http://mukeshgupta.info","mukeshsgupta@gmail.com")
        ,"Apache Licence Version 2.0"
        ,"https://wwww.apache.org/licenses/LICENSE-2.0",new ArrayList<>());
    }

}
