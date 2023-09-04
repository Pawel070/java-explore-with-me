package ru.practicum.user;

import lombok.experimental.UtilityClass;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserSmDto;
import ru.practicum.user.model.User;

@UtilityClass
public class UserMapper {
    public User toUser(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public UserSmDto toUserSmDto(User user) {
        return UserSmDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

}