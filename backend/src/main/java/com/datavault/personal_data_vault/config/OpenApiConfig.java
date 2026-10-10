```java
package com.datavault.personal_data_vault.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        String securitySchemeName = "bearerAuth";

        return new OpenAPI()
            .info(new Info()
                .title("Personal Data Vault & Consent Management API")
                .version("1.0.0")
                .description(
                    "Secure backend for managing personal data and "
                    + "third-party consent. Registration, login, token "
                    + "refresh, and Swagger UI are public. Protected "
                    + "endpoints require the appropriate JWT and role."
                )
            )
            .components(new Components()
                .addSecuritySchemes(
                    securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            );
    }
}
```
