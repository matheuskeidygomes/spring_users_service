package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration                                  // Indica que essa classe é uma classe de configuração (Será identificada automaticamente pelo Spring, desde que esteja no pacote base ou em um subpacote)
public class TimezoneConfig {
    @PostConstruct                              // Indica que o método será executado após a inicialização do contexto do Spring
    public void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}

