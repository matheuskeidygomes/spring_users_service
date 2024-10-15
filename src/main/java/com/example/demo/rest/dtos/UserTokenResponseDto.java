package com.example.demo.rest.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserTokenResponseDto extends UserResponseDto {
    private String token;
}
