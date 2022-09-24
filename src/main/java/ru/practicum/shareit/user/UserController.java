package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final IUserService iUserService;

    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> readAll() {
        final Optional<List<UserDto>> users = iUserService.readAll();
        log.debug("Текущее количество пользователей: {}", users.get().size());
        return !users.isEmpty()
                ? new ResponseEntity<>(users.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> read(@PathVariable(name = "id") int id) {
        final Optional<UserDto> user = iUserService.read(id);

        return !user.isEmpty()
                ? new ResponseEntity<>(user.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto) {

        Optional<UserDto> newUser = iUserService.create(userDto);
        log.debug(String.valueOf(newUser));

        return !newUser.isEmpty()
                ? new ResponseEntity<>(newUser.get(), HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto,
                                          @PathVariable(name = "id") int id) {

        final boolean updated = iUserService.update(userDto, id);

        return updated
                ? new ResponseEntity<>(iUserService.read(id).get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        final boolean deleted = iUserService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
