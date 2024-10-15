package com.example.demo.rest.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @Size(min = 6, message = "Password must have at least 6 characters")     
    @NotBlank(message = "Password is required")
    private String password;
}
