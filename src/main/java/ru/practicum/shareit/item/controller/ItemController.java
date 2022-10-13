package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.IItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final IItemService iItemService;

    public ItemController(IItemService iItemService) {
        this.iItemService = iItemService;
    }

    @GetMapping
    public List<ItemInfoDto> readAllUserItems(@RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return iItemService.readAllUserItems(userId);
    }

    @GetMapping(value = "/{id}")
    public ItemInfoDto read(@PathVariable(name = "id") int id,
                            @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return iItemService.read(id, userId);
    }

    @GetMapping(value = "/search")
    public List<ItemInfoDto> search(@RequestParam String text) {
        return iItemService.searchItemByWord(text);
    }

    @PostMapping
    public ItemInfoDto create(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return iItemService.create(itemDto, userId);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemInfoDto update(
            @RequestHeader(name = "X-Sharer-User-Id") int userId,
            @RequestBody ItemDto itemDto,
            @PathVariable int itemId) {

        return iItemService.update(itemDto, userId, itemId);
    }

    @DeleteMapping(value = "/{id}")
    public HttpStatus delete(@PathVariable(name = "id") int id,
                             @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return iItemService.delete(id, userId);
    }

    @DeleteMapping
    public HttpStatus deleteAllUserItems(@RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return iItemService.deleteAllUserItems(userId);

    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentInfoDto createComment(@Valid @RequestBody CommentInfoDto commentInfoDto,
                                        @RequestHeader(name = "X-Sharer-User-Id") int userId,
                                        @PathVariable(name = "itemId") int itemId) {
        return iItemService.createComment(commentInfoDto, itemId, userId);
    }
}
