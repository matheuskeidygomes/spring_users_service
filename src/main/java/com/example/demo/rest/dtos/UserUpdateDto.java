package com.example.demo.rest.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserUpdateDto {
    @Email(message = "Invalid email")
    @Size(min = 1, message = "Email must not be empty")
    private String email;

    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    @Size(min = 1, message = "Role must not be empty")
    @Pattern(regexp = "^(ADMIN|USER)$", message = "Role must be one of ADMIN or USER")
    private String role;
}
