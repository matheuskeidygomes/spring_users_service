package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration                                              // Indica que essa classe é uma classe de configuração
public class CorsConfig implements WebMvcConfigurer {             // Classe para configurar o CORS (Cross-Origin Resource Sharing) para permitir requisições de outros domínios
    @Override
    public void addCorsMappings(CorsRegistry registry) {    // Sobrescreve o método addCorsMappings para configurar o CORS para permitir requisições de qualquer origem
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
    }
}
