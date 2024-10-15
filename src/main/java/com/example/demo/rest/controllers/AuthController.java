package com.example.demo.rest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.rest.dtos.UserLoginDto;
import com.example.demo.rest.dtos.UserRegisterDto;
import com.example.demo.rest.dtos.UserTokenResponseDto;
import com.example.demo.rest.common.ErrorMessage;
import com.example.demo.rest.mappers.UserMapper;
import com.example.demo.services.TokenService;
import com.example.demo.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Auth", description = "Endpoints para autenticação de usuários")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
  private final TokenService tokenService;
  private final UserService userService;
  private final AuthenticationManager authenticationManager;

  @Operation(summary = "Logar usuário", description = "Realizar login de usuário na base de dados", responses = {
      @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserTokenResponseDto.class))),
      @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
  })
  @PostMapping("login")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<UserTokenResponseDto> login(@RequestBody @Valid UserLoginDto loginDto, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());  // Cria um token de autenticação com email e senha
    authenticationManager.authenticate(authenticationToken);  // authenticationManager irá chamar o método loadUserByUsername da classe UserDetailsService para validar o usuário do token

    User user = userService.findByEmail(loginDto.getEmail());
    String token = tokenService.generateToken(user);
  
    return ResponseEntity.ok(UserMapper.toTokenDto(user, token));
  }

  @Operation(summary = "Cadastrar um usuário", description = "Cadastra um novo usuário na base de dados", responses = {
      @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserTokenResponseDto.class))),
      @ApiResponse(responseCode = "409", description = "Email já cadastrado na base de dados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "422", description = "Argumentos de criação inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
  })
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<UserTokenResponseDto> register(@Valid @RequestBody UserRegisterDto registerDto) {
    User user = userService.save(UserMapper.toUser(registerDto));
    String token = tokenService.generateToken(user);

    return ResponseEntity.ok(UserMapper.toTokenDto(user, token));
  }
}
