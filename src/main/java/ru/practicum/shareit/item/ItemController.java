package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
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
    public ResponseEntity<List<ItemDto>> readAllUserItems(@RequestHeader(name = "X-Sharer-User-Id", required = true) int userId) {
        final Optional<List<ItemDto>> items = iItemService.readAllUserItems(userId);
        return !items.isEmpty()
                ? new ResponseEntity<>(items.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ItemDto> read(@PathVariable(name = "id") int id) {
        final ItemDto itemDto = iItemService.read(id);

        return itemDto != null
                ? new ResponseEntity<>(itemDto, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam String text) {
        final Optional<List<ItemDto>> items = iItemService.searchItemByWord(text);

        return !items.isEmpty()
                ? new ResponseEntity<>(items.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ItemDto> create(@Valid @RequestBody ItemDto itemDto,
                                          @RequestHeader(name = "X-Sharer-User-Id") int userId
    ) {
        final ItemDto newItem = iItemService.create(itemDto, userId);
        log.debug(String.valueOf(newItem));
        return newItem != null
                ? new ResponseEntity<>(newItem, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<ItemDto> update(
            @RequestHeader(name = "X-Sharer-User-Id") int userId,
            @RequestBody ItemDto itemDto,
            @PathVariable int itemId) {

        final boolean updated = iItemService.update(itemDto, userId, itemId);
        return updated
                ? new ResponseEntity<>(iItemService.read(itemId), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id,
                                    @RequestHeader(name = "X-Sharer-User-Id", required = true) int userId) {
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

}
