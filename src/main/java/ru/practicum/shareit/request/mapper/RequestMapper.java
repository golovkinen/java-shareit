package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RequestMapper {
    public static Request createRequest(RequestDto itemRequestDto, User requester) {
        return new Request(null, itemRequestDto.getDescription(), LocalDateTime.now(),
                requester, null);
    }

    public static RequestDto toRequestDto(Request request) {

        if (request.getRequestResponses() == null || request.getRequestResponses().isEmpty()) {
            return new RequestDto(request.getId(), request.getDescription(), request.getCreated(),
                    request.getUser().getId(), new ArrayList<>());
        }

        return new RequestDto(request.getId(), request.getDescription(), request.getCreated(),
                request.getUser().getId(), request.getRequestResponses().stream()
                .map(f -> new RequestDto.ItemsForRequestDto(f.getId(), f.getName(),
                        f.getDescription(), f.getAvailable(), request.getId())).
                collect(Collectors.toList()));
    }
}
