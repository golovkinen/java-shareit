package ru.practicum.shareit.item.model;

import java.util.List;
import java.util.Optional;

public interface IItemService {
    Item create(Item item, int userId);

    List<Item> readAll();

    List<Item> readAllUserItems(int id);

    Optional<Item> read(int id);

    boolean update(Item item, int userId, int itemId);

    boolean delete(int id, int userId);

    boolean deleteAllUserItems(int id);

    List<Item> searchItemByWord(String searchSentence);
}
