package com.example.demo.rest.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponseDto {
    private Long id;
    private String email;
    private String role;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}
