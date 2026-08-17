package com.mbemnova.recensement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {

    @Bean
    public OpenAPI recensementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Recensement National du Cameroun")
                        .description("Recensement de la population, menage par menage, zone par zone.")
                        .version("1.0.0")
                        .contact(new Contact().name("MbemNova").email("contact@mbemnova.cm")));
    }

}
