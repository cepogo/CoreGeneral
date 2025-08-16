package com.banquito.core.general.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI (Swagger) para el servicio de General.
 * 
 * Esta configuración define la documentación de la API y los servidores disponibles,
 * incluyendo el servidor de producción en AWS ALB.
 * 
 * @author Banquito Core Team
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura la información de OpenAPI para la documentación de la API.
     * 
     * @return Configuración de OpenAPI personalizada
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banquito Core - General API")
                        .description("API REST para la gestión de información general del sistema bancario, " +
                                "incluyendo entidades bancarias, ubicaciones geográficas, monedas, feriados y sucursales.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Banquito Core Team")
                                .email("support@banquito.com")
                                .url("https://banquito.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://banquito-alb-1166574131.us-east-2.elb.amazonaws.com/api/general")
                                .description("Servidor de Producción - AWS ALB"),
                        new Server()
                                .url("http://localhost/api/general")
                                .description("Servidor Local - Desarrollo"),
                        new Server()
                                .url("http://localhost:8080/api/general")
                                .description("Servidor Local - Puerto 8080")
                ));
    }
}
