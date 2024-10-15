package com.example.demo.rest.mappers;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.example.demo.entities.User;
import com.example.demo.rest.dtos.UserResponseDto;
import com.example.demo.rest.dtos.UserTokenResponseDto;

public class UserMapper {
    public static User toUser(Object userDto) {
        return new ModelMapper().map(userDto, User.class);  // O método map do ModelMapper mapeia um objeto de um tipo para outro, neste caso, de UserDto para User
    }

    public static UserResponseDto toDto(User user) {
        return new ModelMapper().map(user, UserResponseDto.class);  // O método map do ModelMapper mapeia um objeto de um tipo para outro, neste caso, de User para UserResponse
    }

    public static List<UserResponseDto> toDtoList(List<User> users) {
        return users.stream().map(user -> new ModelMapper().map(user, UserResponseDto.class)).toList();
    }

    public static UserTokenResponseDto toTokenDto(User user, String token) {
        ModelMapper modelMapper = new ModelMapper();
        UserTokenResponseDto userTokenResponse = modelMapper.map(user, UserTokenResponseDto.class);
        userTokenResponse.setToken(token);
        return userTokenResponse;
    }
}