package ru.practicum.shareit.user.model;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    Optional<UserDto> create(UserDto userDto);

    Optional<List<UserDto>> readAll();

    Optional<UserDto> read(int id);

    boolean update(UserDto userDto, int userId);

    boolean delete(int id);

    Optional<User> getUser(int userId);
}
