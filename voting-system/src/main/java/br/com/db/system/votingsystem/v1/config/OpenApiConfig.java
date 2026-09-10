package br.com.db.system.votingsystem.v1.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Voting System API",
                version = "1.0",
                description = "API for managing voting sessions, members and votes"
        )
)
public class OpenApiConfig {
}