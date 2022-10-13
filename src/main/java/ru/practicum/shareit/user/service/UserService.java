package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptionhandler.ConflictException;
import ru.practicum.shareit.exceptionhandler.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.IUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements IUserService {

    private final IUserRepository iUserRepository;

    public UserService(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public UserDto create(UserDto userDto) {

        return UserMapper.toUserDto(iUserRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public List<UserDto> readAll() {
        List<User> usersList = iUserRepository.findAll();
        if (usersList.isEmpty()) {
            log.error("NotFoundException: {}", "Пользователи не найдены");
            throw new NotFoundException("Пользователи не найдены");
        }
        return usersList.stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto read(int userId) {
        Optional<User> user = iUserRepository.findById(userId);
        if (user.isEmpty()) {
            log.error("NotFoundException: {}", "При чтении из БД пользователя, Пользователь с ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }
        return UserMapper.toUserDto(user.get());
    }

    @Override
    public UserDto update(UserDto userDto, int userId) {

        Optional<User> userToUpdate = iUserRepository.findById(userId);

        if (userToUpdate.isEmpty()) {
            log.error("NotFoundException: {}", "При обновлении пользователя, Пользователь с ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }

        if (iUserRepository.findByEmail(userDto.getEmail()).isPresent()) {
            log.error("ConflictException: {}", "При обновлении пользователя, Пользователь с с email " + userDto.getEmail() + " уже существует");
            throw new ConflictException("Пользователь с email " + userDto.getEmail() + " уже существует");
        }

        if (userDto.getName() != null) {
            userToUpdate.get().setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userToUpdate.get().setEmail(userDto.getEmail());
        }

        return UserMapper.toUserDto(iUserRepository.save(userToUpdate.get()));

    }

    @Override
    public HttpStatus delete(int userId) {
        if (iUserRepository.findById(userId).isEmpty()) {
            log.error("NotFoundException: {}", "При удалении пользователя, Пользователь с ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }
        iUserRepository.deleteById(userId);
        return HttpStatus.OK;
    }

    @Override
    public Optional<User> getUser(int userId) {
        return iUserRepository.findById(userId);
    }
}
