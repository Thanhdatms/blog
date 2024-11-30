package com.group7.blog.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    /**
     * Configures the OpenAPI (Swagger) documentation for the application.
     * Includes API information, security schemes, external documentation, and multiple server URLs.
     *
     * @return the customized OpenAPI object
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Bearer Authentication";

        // API Information
        Info apiInfo = new Info()
                .title("Authentication Service API")
                .description("This service is responsible for validating user credentials and providing authentication features.")
                .version("v1.0.0")
                .license(new License()
                        .name("Apache License 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));

        // External Documentation
        ExternalDocumentation externalDocs = new ExternalDocumentation()
                .description("Spring Boot Wiki Documentation")
                .url("https://github.com/spring-projects/spring-boot/wiki");

        // Security Scheme
        SecurityScheme securityScheme = new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // Server URLs
        Server developmentServer = new Server()
                .url("http://localhost:8080")
                .description("Development");

        Server productionServer = new Server()
                .url("https://www.yourrlove.com")
                .description("Production");

        // Combine everything into the OpenAPI object
        return new OpenAPI()
                .info(apiInfo)
                .externalDocs(externalDocs)
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components().addSecuritySchemes(securitySchemeName, securityScheme))
                .servers(Arrays.asList(developmentServer, productionServer));
    }
}
