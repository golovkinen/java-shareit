package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;
import java.util.Optional;

public interface IItemService {
    ItemInfoDto create(ItemDto itemDto, int userId);

    Optional<List<ItemInfoDto>> readAll();

    Optional<List<ItemInfoDto>> readAllUserItems(int id);

    ItemInfoDto read(int id, int userId);

    boolean update(ItemDto itemDto, int userId, int itemId);

    boolean delete(int id, int userId);

    boolean deleteAllUserItems(int id);

    Optional<List<ItemInfoDto>> searchItemByWord(String searchSentence);

    Optional<CommentInfoDto> createComment(CommentCreateDto commentCreateDto, Integer itemId, Integer userId);
}
