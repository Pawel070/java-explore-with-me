package ru.practicum.user;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserSmDto;
import ru.practicum.user.model.User;

@Mapper(componentModel = "spring", uses = {Event.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    static User toUser(UserDto userDto) {
        return null;
    }

    static UserDto toUserDto(User user) {
        return null;
    }

    static UserSmDto toUserSmDto(User user) {
        return null;
    }

}
/*
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

 */