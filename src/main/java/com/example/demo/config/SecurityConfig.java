package com.example.demo.config;

import com.example.demo.auth.AuthEntrypoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.auth.AuthFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity          // Habilita o uso do Spring Security para segurança da aplicação
@EnableMethodSecurity       // Habilita o uso de anotações do Spring Security para autorização de acesso aos endpoints (como @PreAuthorize)
@Configuration
public class SecurityConfig {
    private final AuthFilter authFilter;

    private static final String[] DOCUMENTATION_OPENAPI = {
            "/docs/index.html",
            "/v3/api-docs/**",
            "/swagger-ui-custom.html", "/swagger-ui.html", "/swagger-ui/**",
            "/**.html", "/webjars/**", "/configuration/**", "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {        // Configura o filtro de segurança do Spring Security
        return http
                .csrf(csrf -> csrf.disable())                                                                           // Desabilita o CSRF (Cross-Site Request Forgery) 
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))           // Configura que a autenticação será Stateless (sem sessão)
                .authorizeHttpRequests(authorize -> authorize                                                           // Configura as autorizações das requisições
                        .requestMatchers(HttpMethod.POST, "auth/**").permitAll()                               // Permite o acesso ao endpoint de autenticação
                        //.requestMatchers(HttpMethod.GET, "users").hasRole("ADMIN")                                    // Permite o acesso aos endpoints de busca de usuários apenas para usuários com papel ADMIN
                        .requestMatchers(DOCUMENTATION_OPENAPI).permitAll()                                             // Permite o acesso aos endpoints de documentação
                        .anyRequest().authenticated()                                                                   // Qualquer outra requisição precisa de autenticação
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)                   // Adiciona o filtro de autenticação antes do filtro de autenticação padrão do Spring Security
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new AuthEntrypoint())) // Configura o tratamento de exceções de autenticação
                .build();
    }

    @Bean 
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {    // Configura o gerenciador de autenticação do Spring Security
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {      // Configura o codificador de senhas do Spring Security (BCrypt)
        return new BCryptPasswordEncoder();
    }
}

