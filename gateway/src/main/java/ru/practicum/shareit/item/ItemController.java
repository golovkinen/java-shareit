package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> readAllUserItems(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return itemClient.readAllUserItems(userId, from, size);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> readAllItems(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return itemClient.readAll(from, size);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> read(@PathVariable(name = "itemId") int itemId,
                                       @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return itemClient.searchItemByWord(text, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemDto itemDto,
                                         @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> update(
            @RequestHeader(name = "X-Sharer-User-Id") int userId,
            @RequestBody ItemDto itemDto,
            @PathVariable int itemId) {

        return itemClient.updateItem(itemDto, userId, itemId);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(name = "id") int id,
                                         @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return itemClient.delete(id, userId);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAllUserItems(@RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return itemClient.deleteAllUserItems(userId);

    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentInfoDto commentInfoDto,
                                                @RequestHeader(name = "X-Sharer-User-Id") int userId,
                                                @PathVariable(name = "itemId") int itemId) {
        return itemClient.createComment(commentInfoDto, userId, itemId);
    }

}
