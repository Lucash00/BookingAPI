package com.bookingapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Booking API")
                .version("1.0.0")
                .description("Documentación interactiva para la API de reservas")
                .contact(new Contact()
                    .name("Lucas Moreno Corral")
                    .email("lucas.moreno.dev@gmail.com")
                    .url("https://www.lucas-moreno-dev.com/"))
                .license(new License()
                    .name("MIT")
                    .url("https://opensource.org/licenses/MIT")));
    }
}