package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.IItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final IItemService iItemService;

    public ItemController(IItemService iItemService) {
        this.iItemService = iItemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemInfoDto>> readAllUserItems(@RequestHeader(name = "X-Sharer-User-Id") int userId) {
        final Optional<List<ItemInfoDto>> items = iItemService.readAllUserItems(userId);
        return !items.isEmpty()
                ? new ResponseEntity<>(items.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ItemInfoDto> read(@PathVariable(name = "id") int id,
                                            @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        final ItemInfoDto itemDto = iItemService.read(id, userId);

        return itemDto != null
                ? new ResponseEntity<>(itemDto, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<ItemInfoDto>> search(@RequestParam String text) {
        final Optional<List<ItemInfoDto>> items = iItemService.searchItemByWord(text);

        return !items.isEmpty()
                ? new ResponseEntity<>(items.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ItemInfoDto> create(@Valid @RequestBody ItemDto itemDto,
                                              @RequestHeader(name = "X-Sharer-User-Id") int userId
    ) {
        final ItemInfoDto newItem = iItemService.create(itemDto, userId);
        log.debug(String.valueOf(newItem));
        return newItem != null
                ? new ResponseEntity<>(newItem, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<ItemInfoDto> update(
            @RequestHeader(name = "X-Sharer-User-Id") int userId,
            @RequestBody ItemDto itemDto,
            @PathVariable int itemId) {

        final boolean updated = iItemService.update(itemDto, userId, itemId);
        return updated
                ? new ResponseEntity<>(iItemService.read(itemId, userId), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id,
                                    @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        final boolean deleted = iItemService.delete(id, userId);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestHeader(name = "X-Sharer-User-Id") int userId) {
        final boolean deleted = iItemService.deleteAllUserItems(userId);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<CommentInfoDto> createComment(@Valid @RequestBody CommentCreateDto commentCreateDto,
                                                        @RequestHeader(name = "X-Sharer-User-Id") int userId,
                                                        @PathVariable(name = "itemId") int itemId) {
        final Optional<CommentInfoDto> newComment = iItemService.createComment(commentCreateDto, itemId, userId);

        return !newComment.isEmpty()
                ? new ResponseEntity<>(newComment.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

   /* @PatchMapping(value = "/{itemId}/comment")
    public ResponseEntity<CommentInfoDto> updateComment (@RequestBody CommentCreateDto commentCreateDto,
                                                       @RequestHeader(name = "X-Sharer-User-Id") int userId,
                                                       @PathVariable(name = "itemId") int itemId) {
        final boolean updated = iItemService.updateComment(commentCreateDto, userId, itemId);
        return updated
                ? new ResponseEntity<>(iItemService.readComment(itemId), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{itemId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment (@RequestBody CommentCreateDto commentCreateDto,
                                                         @RequestHeader(name = "X-Sharer-User-Id") int userId,
                                                         @PathVariable(name = "itemId") int itemId,
                                                         @PathVariable(name = "commentId") int commentId) {
        final boolean deleted = iItemService.deleteComment(commentCreateDto, userId, itemId, commentId);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } */
}
