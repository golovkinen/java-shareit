package ru.practicum.shareit.item.service;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

public interface IItemService {
    ItemInfoDto create(ItemDto itemDto, int userId);

    List<ItemInfoDto> readAll(int from, int size);

    List<ItemInfoDto> readAllUserItems(int id, int from, int size);

    ItemInfoDto read(int id, int userId);

    ItemInfoDto update(ItemDto itemDto, int userId, int itemId);

    HttpStatus delete(int id, int userId);

    HttpStatus deleteAllUserItems(int id);

    List<ItemInfoDto> searchItemByWord(String searchSentence, int from, int size);

    CommentInfoDto createComment(CommentInfoDto commentInfoDto, Integer itemId, Integer userId);
}
