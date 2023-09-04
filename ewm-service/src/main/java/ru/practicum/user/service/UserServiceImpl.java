package ru.practicum.user.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    Pageable pageable;

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        log.info("UserServiceImpl addUser: Добавление пользователя -> " + user);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> findUsers(List<Long> uin, int from, int size) {
        log.info("UserServiceImpl findUsers: uin {}, from {}, size {}", uin, from, size);
        pageable = PageRequest.of(from / size, size);
        if (uin == null || uin.isEmpty()) {
            return userRepository.findAll(pageable).getContent().stream()
                    .map(UserMapper::toUserDto).collect(Collectors.toList());
        } else {
            return userRepository.findByIdIn(uin, pageable).getContent().stream()
                    .map(UserMapper::toUserDto).collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
        log.info("UserServiceImpl deleteUser: Удаление пользователя -> " + user);
        userRepository.delete(user);
        log.info("UserServiceImpl deleteUser: Пользователь удалён.");
    }

}