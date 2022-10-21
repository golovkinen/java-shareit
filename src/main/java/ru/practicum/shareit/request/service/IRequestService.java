package ru.practicum.shareit.request.service;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface IRequestService {

    List<RequestDto> readAllUserRequests(int userId);

    List<RequestDto> readAll(int from, int size, int userId);

    RequestDto read(int requestId, int userId);

    RequestDto create(RequestDto requestDto, int userId);

    RequestDto update(RequestDto requestDto, int userId, int requestId);

    HttpStatus delete(int userId, int requestId);
}
