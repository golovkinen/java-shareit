package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User create(User user);

    List<User> readAll();

    Optional<User> read(int id);

    boolean update(User user, int userId);

    boolean delete(int id);
}
