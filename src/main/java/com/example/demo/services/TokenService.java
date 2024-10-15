package com.example.demo.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.entities.User;
import com.example.demo.rest.exceptions.UnauthorizedException;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration-time}")
    private Long expirationTime;

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);            // Criando um algoritmo de criptografia HMAC256 com a chave secreta definida
            String token = JWT.create()                                 // Criando um token JWT
                    .withIssuer("spring-demo-api")               // Definindo o emissor do token
                    .withSubject(user.getEmail())                       // Definindo o sujeito do token (email do usuário)
                    .withExpiresAt(generateExpirationDate())            // Definindo a data de expiração do token
                    .sign(algorithm);                                   // Assinando o token com o algoritmo criado
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao criar token", e);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);            // Criando um algoritmo de criptografia HMAC256 com a chave secreta definida
            return JWT.require(algorithm)                               // Verificando a assinatura do token de acordo com o algoritmo criado
                    .withIssuer("spring-demo-api")               // Verificando o emissor do token
                    .build()                                            // Construindo o verificador do token
                    .verify(token)                                      // Verificando o token
                    .getSubject();                                      // Retornando o sujeito do token
        } catch (JWTVerificationException e){
            throw new UnauthorizedException("Token inválido");
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(expirationTime).toInstant(ZoneOffset.UTC); // Gerando a data de expiração do token com base no tempo de expiração definido
    }
}