package com.example.demo.auth;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.rest.exceptions.UnauthorizedException;
import com.example.demo.services.TokenService;
import com.example.demo.services.AuthService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthFilter extends OncePerRequestFilter {
  private final TokenService tokenService;
  private final AuthService authService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = recoverToken(request);                             // Recupera o token da requisição

    if (token == null) {
      filterChain.doFilter(request, response);                        // Se o token não for informado, continua o fluxo da requisição sem autenticação
      return;
    }

    String email = tokenService.validateToken(token);                 // Valida o token e recupera o email do usuário
    UserDetails user = authService.loadUserByUsername(email);         // Busca o usuário no banco de dados a partir do subjet do token (email)
      
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());  // Cria um token de autenticação
    SecurityContextHolder.getContext().setAuthentication(authentication);                                                                       // Seta o usuário autenticado no contexto de segurança do Spring Security

    filterChain.doFilter(request, response);                          // Continua o fluxo da requisição com o usuário autenticado
  }

  private String recoverToken(HttpServletRequest request) throws UnauthorizedException {
    String token = request.getHeader("Authorization");

    if (token == null || !token.startsWith("Bearer ")) {
      return null;
    }

    return token.substring(7);
  }
}
