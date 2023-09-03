package ru.practicum.user.service;

import java.util.List;

import ru.practicum.user.dto.UserDto;

public interface UserService {

    UserDto addUser(UserDto userDto);

    List<UserDto> findUsers(List<Long> uin, int from, int size);

    void deleteUser(Long id);

}