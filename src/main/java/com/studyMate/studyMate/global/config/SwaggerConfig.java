package com.studyMate.studyMate.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "스터디 메이트 API 문서",
                description = "스터디메이트 API 서버 문서",
                version = "v1"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "로컬 환경"),
                @Server(url = "https://study-mate-bff-ecb080c0db60.herokuapp.com/", description = "dev")
        }
)
@Configuration
public class SwaggerConfig {

    private static final String BEARER_TOKEN_PREFIX = "Bearer";

    @Bean
    public OpenAPI openAPI() {
        String securityName = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityName);
        Components components = new Components()
                .addSecuritySchemes(securityName, new SecurityScheme().name(securityName).type(SecurityScheme.Type.HTTP).scheme(BEARER_TOKEN_PREFIX).bearerFormat(securityName));

        return new OpenAPI()
                .components(components)
                .addSecurityItem(securityRequirement);
    }}
