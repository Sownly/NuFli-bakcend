package com.sownly.nufli.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI nufliOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("NuFli Ecosystem API")
                .description("Unified Backend API for Nutrity (Nutrition App) and Flitness (Fitness App)")
                .version("1.0.0")
                .contact(new Contact().name("Sownly Team").email("support@sownly.com"))
                .license(new License().name("Apache 2.0")));
    }
}
