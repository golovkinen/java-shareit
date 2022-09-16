package ru.practicum.shareit.user;

public interface IUserRepositoryCustom {


    boolean updateUser(User user, int userId);

    boolean deleteById(int id);

    void deleteAll();
}
