package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@EnableWebMvc
@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components().addSecuritySchemes("security", securityScheme()))
        .info(
          new Info().title("API de Usuários")
            .description("API para cadastro de usuários")
            .version("v1")
        );
  }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .description("Insira um bearer token valido para prosseguir")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("security");
    }
}
