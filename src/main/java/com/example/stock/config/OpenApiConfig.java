package com.example.stock.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for Stock Management API.
 * Provides comprehensive API documentation via Swagger UI.
 * 
 * @author Generated
 * @since 1.0
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Stock Management API",
        version = "1.0.0",
        description = "Professional REST API for comprehensive stock and inventory management system. " +
                     "Provides complete CRUD operations, advanced filtering, search capabilities, " +
                     "and business logic for branches, categories, suppliers, and inventory items.",
        contact = @Contact(
            name = "Stock Management Team",
            email = "support@BlinkDoc.com",
            url = "https://BlinkStock.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            description = "Development Server",
            url = "http://localhost:8083"
        ),
        @Server(
            description = "Production Server",
            url = "https://api.stockmanagement.com"
        )
    }
)
public class OpenApiConfig {
    
    // Configuration is handled via annotations above
    // Additional customization can be added here if needed
    
}
