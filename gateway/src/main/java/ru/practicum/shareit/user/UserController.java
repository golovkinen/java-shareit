package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
	private final UserClient userClient;

	@GetMapping
	public ResponseEntity<Object> readAll() {
		return userClient.readAll();
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> read(@PathVariable(name = "id") int id) {
		return userClient.read(id);
	}

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {

		return userClient.create(userDto);
	}

	@PatchMapping(value = "/{id}")
	public ResponseEntity<Object> update(@RequestBody UserDto userDto,
										 @PathVariable(name = "id") int id) {
		return userClient.update(userDto, id);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> delete(@PathVariable(name = "id") int id) {
		return userClient.delete(id);
	}

}
