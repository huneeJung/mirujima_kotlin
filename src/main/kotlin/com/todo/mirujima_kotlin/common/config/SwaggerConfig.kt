package com.todo.mirujima_kotlin.common.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(servers = [
    Server(url = "https://api.mirujima.shop"),
    Server(url = "http://localhost:8081")
])
@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI() : OpenAPI{
        return OpenAPI()
            .info(Info().title("Mirujima").version("1.0"))
            .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
            .components(Components()
                .addSecuritySchemes("bearerAuth",SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .`in`(SecurityScheme.In.HEADER)
                    .name("Authorization")));
    }

}