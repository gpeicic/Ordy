package com.example.eureka.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ordy API")
                        .description("B2B SaaS aplikacija za praćenje cijena dobavljača i upravljanje narudžbama")
                        .version("1.0.0"));
    }
}
