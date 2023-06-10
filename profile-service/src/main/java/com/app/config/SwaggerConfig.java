package com.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Gabin Ishimwe",
                        email = "g.ishimwe@alustudent.com",
                        url = "https://gabin-portfolio.vercel.app/"
                ),
                description = "Profile-service OpenApi swagger documentation",
                title = "Profile-service OpenApi doc",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local Env",
                        url = "http://localhost:8081"
                ),
                @Server(
                        description = "Production Env",
                        url = "https://138.68.107.35/"
                ),
        },
        security = {
                @SecurityRequirement(
                        name = "BearerAuth"
                )
        }

)
@SecurityScheme(
        name = "BearerAuth",
        description = "JWT auth security",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
