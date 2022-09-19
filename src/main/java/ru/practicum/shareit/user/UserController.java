package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final IUserService iUserService;

    @Autowired
    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @GetMapping
    public ResponseEntity<Collection<User>> readAll() {
        final Collection<User> users = iUserService.readAll();
        log.debug("Текущее количество пользователей: {}", users.size());
        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> read(@PathVariable(name = "id") int id) {
        final Optional<User> user = iUserService.read(id);

        return !user.isEmpty()
                ? new ResponseEntity<>(user.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {

        User newUser = iUserService.create(user);
        log.debug(String.valueOf(newUser));

        return newUser != null
                ? new ResponseEntity<>(newUser, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<User> update(@RequestBody User user,
                                       @PathVariable(name = "id") int id) {

        final boolean updated = iUserService.update(user, id);

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
