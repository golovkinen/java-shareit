package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.IUserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final IUserService iUserService;

    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @GetMapping
    public List<UserDto> readAll() {
        return iUserService.readAll();
    }

    @GetMapping(value = "/{id}")
    public UserDto read(@PathVariable(name = "id") int id) {
        return iUserService.read(id);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {

        return iUserService.create(userDto);
    }

    @PatchMapping(value = "/{id}")
    public UserDto update(@RequestBody UserDto userDto,
                          @PathVariable(name = "id") int id) {

        return iUserService.update(userDto, id);
    }

    @DeleteMapping(value = "/{id}")
    public HttpStatus delete(@PathVariable(name = "id") int id) {
        return iUserService.delete(id);
    }
}
