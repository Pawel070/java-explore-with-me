package ru.practicum.user.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

@Slf4j
@Validated
@RequestMapping("/admin")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        UserDto userDto1 = userService.addUser(userDto);
        log.info("UsersController addUser: Регистрация нового пользователя: " + userDto1.toString());
        return userDto1;
    }

    @GetMapping("/users")
    public List<UserDto> findUsers(@RequestParam(required = false) List<Long> ids,
                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                   @RequestParam(defaultValue = "10") @Positive int size) {
        List<UserDto> resulting = userService.findUsers(ids, from, size);
        log.info("UsersController findUsers: Cписок пользователей.");
        return resulting;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        log.info("UsersController deleteUser: Удаление пользователя с УИН {}", id);
    }
}