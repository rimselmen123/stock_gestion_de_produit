package com.example.stock.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Application configuration for the Stock Management System.
 * Configures CORS, OpenAPI documentation, and other application settings.
 * 
 * @author Generated
 * @since 1.0
 */
@Configuration
public class ApplicationConfig {

        @Value("${spring.application.name:Stock Management System}")
        private String applicationName;

        /**
         * Configures CORS for the application.
         * Allows cross-origin requests from frontend applications.
         */
        @Bean
        public WebMvcConfigurer corsConfigurer() {
                return new WebMvcConfigurer() {
                        @Override
                        public void addCorsMappings(@NonNull CorsRegistry registry) {
                                registry.addMapping("/api/**")
                                                .allowedOriginPatterns("*")
                                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                                                .allowedHeaders("*")
                                                .allowCredentials(false)
                                                .maxAge(3600);
                        }
                };
        }

        /**
         * Configures OpenAPI documentation.
         * Provides comprehensive API documentation for developers.
         */
        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title(applicationName + " API")
                                                .description("Complete REST API for Stock Management System including Unit, Category, and Inventory Item management")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("Blink Team")
                                                                .email("rimselmen123@gmail.com")
                                                                .url("https://example.com"))
                                                .license(new License()
                                                                .name("MIT License By blink team")
                                                                .url("https://opensource.org/licenses/MIT")))
                                .servers(List.of(
                                                new Server()
                                                                .url("http://localhost:8083")
                                                                .description("Development Server"),
                                                new Server()
                                                                .url("http://localhost:8085")
                                                                .description("Production Server")));
        }
}
