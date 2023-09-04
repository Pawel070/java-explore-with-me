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