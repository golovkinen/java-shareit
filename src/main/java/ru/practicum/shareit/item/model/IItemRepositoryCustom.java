package ru.practicum.shareit.item.model;

import java.util.List;

public interface IItemRepositoryCustom {
    boolean updateItem(Item item, int userId, int itemId);

    boolean deleteItem(int itemId, int userId);

    List<Item> searchItemByWord(String searchSentence);

}
