package ru.practicum.shareit.user.model;

public interface IUserRepositoryCustom {


    boolean updateUser(User user, int userId);

    boolean deleteById(int id);

    void deleteAll();
}
