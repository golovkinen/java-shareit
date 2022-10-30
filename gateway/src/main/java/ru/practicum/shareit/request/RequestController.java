package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> readAllUserRequests(
            @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return requestClient.readAllUserRequests(userId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> readAll(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                          @Valid @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return requestClient.readAll(userId, from, size);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> read(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                       @PathVariable(name = "requestId") int requestId) {
        return requestClient.read(requestId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody RequestDto requestDto,
                                         @RequestHeader(name = "X-Sharer-User-Id") int userId) {

        return requestClient.create(requestDto, userId);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> update(@RequestBody RequestDto requestDto,
                                         @PathVariable(name = "id") int requestId,
                                         @RequestHeader(name = "X-Sharer-User-Id") int userId) {

        return requestClient.update(requestDto, userId, requestId);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(name = "id") int requestId,
                                         @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return requestClient.delete(userId, requestId);
    }

}
