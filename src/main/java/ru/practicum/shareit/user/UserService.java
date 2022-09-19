package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final IUserRepository iUserRepository;
    private final IUserRepositoryCustom iUserRepositoryCustom;

    public UserService(IUserRepository iUserRepository, IUserRepositoryCustom iUserRepositoryCustom) {
        this.iUserRepository = iUserRepository;
        this.iUserRepositoryCustom = iUserRepositoryCustom;
    }

    @Override
    public User create(User user) {
        if (iUserRepository.findByEmail(user.getEmail()).isPresent()) {
            return null;
        }
        return iUserRepository.save(user);
    }

    @Override
    public List<User> readAll() {
        return iUserRepository.findAll();
    }

    @Override
    public Optional<User> read(int id) {
        return iUserRepository.findById(id);
    }

    @Override
    public boolean update(User user, int userId) {
        return iUserRepositoryCustom.updateUser(user, userId);
    }

    @Override
    public boolean delete(int id) {
        return iUserRepositoryCustom.deleteById(id);
    }
}
