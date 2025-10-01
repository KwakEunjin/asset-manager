package com.assetmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Asset Manager API")
                        .description("가계부 & 자산관리 애플리케이션 API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Asset Manager")
                                .email("contact@assetmanager.com")));
    }
}