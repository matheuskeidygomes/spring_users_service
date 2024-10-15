package com.example.demo.auth;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthEntrypoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException, IOException {
        response.setHeader("WWW-Authenticate", "Bearer realm='/auth/login'");        // Define o tipo de autenticação e onde o token pode ser obtido
        response.setContentType("application/json");                                             // Define o tipo de conteúdo da resposta
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);                                 // Define o status da resposta como não autorizado
        response.getWriter().write("{\"status\": 401, \"message\": \"Não autorizado\"}");    // Escreve a mensagem de erro na resposta
    }
}
