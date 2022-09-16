package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.IItemService;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {

    private final IItemService iItemService;

    @Autowired
    public ItemController(IItemService iItemService) {
        this.iItemService = iItemService;
    }

    @GetMapping
    public ResponseEntity<Collection<Item>> readAllUserItems(@RequestHeader(name = "X-Sharer-User-Id", required = true) int userId) {
        final Collection<Item> items = iItemService.readAllUserItems(userId);
        log.debug("Текущее количество вещей пользователя: {}", items.size());
        return items != null
                ? new ResponseEntity<>(items, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Item> read(@PathVariable(name = "id") int id) {
        final Optional<Item> item = iItemService.read(id);

        return item != null
                ? new ResponseEntity<>(item.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Collection<Item>> search(@RequestParam String text) {
        final List<Item> items = iItemService.searchItemByWord(text);

        return items != null
                ? new ResponseEntity<>(items, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Item> create(@Valid
                                       @RequestHeader(name = "X-Sharer-User-Id", required = true) int userId,
                                       @RequestBody Item item) {

        final Item newItem = iItemService.create(item, userId);
        log.debug(String.valueOf(newItem));
        return newItem != null
                ? new ResponseEntity<>(newItem, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Item> update(
            @RequestHeader(name = "X-Sharer-User-Id", required = true) int userId,
            @RequestBody Item item,
            @PathVariable int itemId) {

        final boolean updated = iItemService.update(item, userId, itemId);
        return updated
                ? new ResponseEntity<>(iItemService.read(itemId).get(), HttpStatus.OK)
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
    public ResponseEntity<?> delete(@RequestHeader(name = "X-Sharer-User-Id", required = true) int userId) {
        final boolean deleted = iItemService.deleteAllUserItems(userId);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
