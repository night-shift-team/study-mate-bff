package com.studyMate.studyMate.global.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile("dev")
@OpenAPIDefinition(
        info = @Info(
                title = "스터디 메이트 API 문서 (Dev)",
                description = "스터디메이트 BFF Server API 문서 (Dev)",
                version = "v1"
        ),
        servers = {
                @Server(url = "https://api-dev.study-mate.academy/api/v1", description = "개발 환경")
        }
)
@Configuration
public class SwaggerConfigDev implements WebMvcConfigurer {

    private static final String BEARER_TOKEN_PREFIX = "Bearer";

    @Bean
    public OpenAPI openAPI() {
        String securityName = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityName);
        Components components = new Components()
                .addSecuritySchemes(securityName, new SecurityScheme()
                        .name(securityName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(BEARER_TOKEN_PREFIX)
                        .bearerFormat(securityName));

        return new OpenAPI()
                .components(components)
                .addSecurityItem(securityRequirement);
    }
}
