package com.example.demo.rest.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRegisterDto {
    @NotBlank(message = "Email is required")            // Indica que o atributo não pode ser nulo ou vazio
    @Email(message = "Invalid email")                   // Indica que o atributo deve ser um email válido
    private String email;

    @Size(min = 6, message = "Password must have at least 6 characters")        // Indica que o tamanho mínimo do atributo é 6
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Role is required")             // Indica que o atributo não pode ser nulo ou vazio
    @Pattern(regexp = "^(ADMIN|USER)$", message = "Role must be one of ADMIN or USER")  // Indica que o atributo deve ser ADMIN ou USER
    private String role;
}
