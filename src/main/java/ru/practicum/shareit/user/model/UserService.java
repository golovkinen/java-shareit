package ru.practicum.shareit.user.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final IUserRepository iUserRepository;
    private final IUserRepositoryCustom iUserRepositoryCustom;

    public UserService(IUserRepository iUserRepository, IUserRepositoryCustom iUserRepositoryCustom) {
        this.iUserRepository = iUserRepository;
        this.iUserRepositoryCustom = iUserRepositoryCustom;
    }

    @Override
    public Optional<UserDto> create(UserDto userDto) {

        return Optional.of(UserMapper.toUserDto(iUserRepository.save(UserMapper.toUser(userDto))));
    }

    @Override
    public Optional<List<UserDto>> readAll() {
        List<User> usersList = iUserRepository.findAll();
        if (usersList == null) {
            return Optional.empty();
        }
        return Optional.of(usersList.stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<UserDto> read(int id) {
        Optional<User> user = iUserRepository.findById(id);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(UserMapper.toUserDto(user.get()));
    }

    @Override
    public boolean update(UserDto userDto, int userId) {
        return iUserRepositoryCustom.updateUser(UserMapper.toUser(userDto), userId);
    }

    @Override
    public boolean delete(int id) {
        return iUserRepositoryCustom.deleteById(id);
    }

    @Override
    public Optional<User> getUser(int userId) {
        return iUserRepository.findById(userId);
    }
}
