package com.giggi.basesetup.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class Swagger {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(
                        new SecurityRequirement().addList("Bearer Authentication")
                )
                .components(
                        new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme())
                )
                .info(
                        new Info()
                                .title("API BaseSetup")
                                .description("Descrizione del servizio API per la gestione di base setup")
                                .version("1.0")
                                .contact(
                                        new Contact()
                                                .name("Luigi Massa")
                                                .email("luigimassa2005@gmail.com")
                                                .url("github.com/GGNado")
                                )
                                .extensions(Map.of(
                                        "x-contacts", List.of(
                                                Map.of(
                                                        "name", "Luigi Massa",
                                                        "email", "luigi.massa@edu.elis.org"
                                                )
                                        )
                                ))
                                .license(new License().name("Copiright (C) 2025 BASESETUP"))
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Production Server"),
                        new Server().url("http://localhost:8080").description("Development Server")
                ));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

}

