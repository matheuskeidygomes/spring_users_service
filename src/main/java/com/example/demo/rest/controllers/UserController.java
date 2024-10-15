package com.example.demo.rest.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.rest.dtos.UserResponseDto;
import com.example.demo.rest.dtos.UserUpdateDto;
import com.example.demo.rest.common.ErrorMessage;
import com.example.demo.rest.mappers.UserMapper;
import com.example.demo.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User", description = "Endpoints para manipulação de usuários") // Anotação do Swagger para documentação da API
@RequiredArgsConstructor            // Gera um construtor com todos os atributos final (para injeção de dependência)
@RestController                     // Indica que essa classe é um controller
@RequestMapping("/users")           // Indica que a URL base para acessar os endpoints desse controller é /users
public class UserController {
    private final UserService userService;

    @Operation(
        summary = "Listar todos usuários",
        security = @SecurityRequirement(name = "security"),
        description = "Retorna uma lista com todos os usuários cadastrados na base de dados",
        responses= {
            @ApiResponse(
                responseCode = "200",
                description = "Usuários listados com sucesso",
                content=@Content(mediaType="application/json", schema=@Schema(implementation= UserResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Erro interno no servidor",
                content=@Content(mediaType="application/json", schema=@Schema(implementation=ErrorMessage.class))
            ),
        }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')") // Anotação do Spring Security para autorização de acesso ao endpoint conforme o papel do usuário
    public ResponseEntity<List<UserResponseDto>> findAll() {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Pega o contexto de autenticação do Spring Security
        // User authenticatedUser = (User) authentication.getPrincipal(); // Pega o usuário autenticado no contexto de autenticação

        return ResponseEntity.ok(UserMapper.toDtoList(userService.findAll())); // ResponseEntity.ok retorna o status 200 (OK) e o objeto passado como argumento
    }

    @Operation(
        summary = "Busca um usuário pelo Id",
        security = @SecurityRequirement(name = "security"),
        description = "Retorna um usuário cadastrado na base de dados pelo Id",
        responses= {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário encontrado e retornado com sucesso",
                content=@Content(mediaType="application/json", schema=@Schema(implementation= UserResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuário não encontrado",
                content=@Content(mediaType="application/json", schema=@Schema(implementation=ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Erro interno no servidor",
                content=@Content(mediaType="application/json", schema=@Schema(implementation=ErrorMessage.class))
            ),
        }
    )
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('USER') AND #id == authentication.principal.id)") // Permissão de acesso apenas para role ADMIN ou para role USER desde que o id do usuário a ser atualizado seja igual ao id do usuário autenticado
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) { // @PathVariable indica que o valor será passado na URL da requisição como /users/1
        return ResponseEntity.ok(UserMapper.toDto(userService.findById(id)));
    }

    @Operation(
        summary = "Atualizar um usuário pelo Id",
        security = @SecurityRequirement(name = "security"),
        description = "Atualize usuário na base de dados pelo Id",
        responses= {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário atualizado com sucesso",
                content=@Content(mediaType="application/json", schema=@Schema(implementation= UserResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Email já cadastrado na base de dados",
                content=@Content(mediaType="application/json", schema=@Schema(implementation=ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "422",
                description = "Argumentos de criação inválidos",
                content=@Content(mediaType="application/json", schema=@Schema(implementation=ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Erro interno no servidor",
                content=@Content(mediaType="application/json", schema=@Schema(implementation=ErrorMessage.class))
            ),
        }
    )
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('USER') AND #id == authentication.principal.id)")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDto updateDto) {
        User user = userService.update(id, UserMapper.toUser(updateDto));
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @Operation(
        summary = "Desativar um usuário",
        security = @SecurityRequirement(name = "security"),
        description = "Desativa um usuário na base de dados",
        responses= {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário desativado com sucesso",
                content=@Content(mediaType="application/json", schema=@Schema(implementation= UserResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Usuário já desativado",
                content=@Content(mediaType="application/json", schema=@Schema(implementation=ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuário não encontrado",
                content=@Content(mediaType="application/json", schema=@Schema(implementation=ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Erro interno no servidor",
                content=@Content(mediaType="application/json", schema=@Schema(implementation=ErrorMessage.class))
            ),
        }
    )
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('USER') AND #id == authentication.principal.id)")
    @PutMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDto> deactivate(@PathVariable Long id) {
        User user = userService.deactivate(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @Operation(
        summary = "Ativar um usuário",
        security = @SecurityRequirement(name = "security"),
        description = "Ativar um usuário na base de dados",
        responses= {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário ativado com sucesso",
                content=@Content(mediaType="application/json", schema=@Schema(implementation= UserResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Usuário já ativado",
                content=@Content(mediaType="application/json", schema=@Schema(implementation=ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Usuário não encontrado",
                content=@Content(mediaType="application/json", schema=@Schema(implementation=ErrorMessage.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Erro interno no servidor",
                content=@Content(mediaType="application/json", schema=@Schema(implementation=ErrorMessage.class))
            ),
        }
    )
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('USER') AND #id == authentication.principal.id)")
    @PutMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDto> activate(@PathVariable Long id) {
        User user = userService.activate(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }
}
