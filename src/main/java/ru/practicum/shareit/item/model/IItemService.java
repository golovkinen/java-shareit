package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Optional;

public interface IItemService {
    ItemDto create(ItemDto itemDto, int userId);

    Optional<List<ItemDto>> readAll();

    Optional<List<ItemDto>> readAllUserItems(int id);

    ItemDto read(int id);

    boolean update(ItemDto itemDto, int userId, int itemId);

    boolean delete(int id, int userId);

    boolean deleteAllUserItems(int id);

    Optional<List<ItemDto>> searchItemByWord(String searchSentence);
}
