package ru.practicum.shareit.item.service;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

public interface IItemService {
    ItemInfoDto create(ItemDto itemDto, int userId);

    List<ItemInfoDto> readAll();

    List<ItemInfoDto> readAllUserItems(int id);

    ItemInfoDto read(int id, int userId);

    ItemInfoDto update(ItemDto itemDto, int userId, int itemId);

    HttpStatus delete(int id, int userId);

    HttpStatus deleteAllUserItems(int id);

    List<ItemInfoDto> searchItemByWord(String searchSentence);

    CommentInfoDto createComment(CommentCreateDto commentCreateDto, Integer itemId, Integer userId);
}
