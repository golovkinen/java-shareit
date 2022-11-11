package ru.practicum.shareit.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.IRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class RequestController {

    private IRequestService iRequestService;

    public RequestController(IRequestService iRequestService) {
        this.iRequestService = iRequestService;
    }

    @GetMapping
    public List<RequestDto> readAllUserRequests(
            @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return iRequestService.readAllUserRequests(userId);
    }

    @GetMapping(value = "/all")
    public List<RequestDto> readAll(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                                    @RequestParam(name = "from", defaultValue = "0") int from,
                                    @RequestParam(name = "size", defaultValue = "10") int size) {
        return iRequestService.readAll(from, size, userId);
    }

    @GetMapping(value = "/{requestId}")
    public RequestDto read(@RequestHeader(name = "X-Sharer-User-Id") int userId,
                           @PathVariable(name = "requestId") int requestId) {
        return iRequestService.read(requestId, userId);
    }

    @PostMapping
    public RequestDto create(@RequestBody RequestDto requestDto,
                             @RequestHeader(name = "X-Sharer-User-Id") int userId) {

        return iRequestService.create(requestDto, userId);
    }

    @PatchMapping(value = "/{id}")
    public RequestDto update(@RequestBody RequestDto requestDto,
                             @PathVariable(name = "id") int requestId,
                             @RequestHeader(name = "X-Sharer-User-Id") int userId) {

        return iRequestService.update(requestDto, userId, requestId);
    }

    @DeleteMapping(value = "/{id}")
    public HttpStatus delete(@PathVariable(name = "id") int requestId,
                             @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return iRequestService.delete(userId, requestId);
    }
}
