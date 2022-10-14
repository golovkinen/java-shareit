package ru.practicum.shareit.user.service;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserDto create(UserDto userDto);

    List<UserDto> readAll();

    UserDto read(int id);

    UserDto update(UserDto userDto, int userId);

    HttpStatus delete(int id);

    Optional<User> getUser(int userId);
}
