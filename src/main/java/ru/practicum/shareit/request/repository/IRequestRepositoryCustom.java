package ru.practicum.shareit.request.repository;

import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface IRequestRepositoryCustom {
    List<Request> getPagedRequests(int from, int size, int userId);
}
