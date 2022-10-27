package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptionhandler.BadRequestException;
import ru.practicum.shareit.exceptionhandler.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.IRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RequestService implements IRequestService {

    private final IUserService iUserService;
    private final IRequestRepository iRequestRepository;

    public RequestService(IUserService iUserService, IRequestRepository iRequestRepository) {
        this.iUserService = iUserService;
        this.iRequestRepository = iRequestRepository;
    }

    @Override
    public List<RequestDto> readAllUserRequests(int userId) {

        Optional<User> user = iUserService.getUser(userId);
        if (user.isEmpty()) {
            log.error("NotFoundException: {}", "Пользователь с ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }

        return iRequestRepository.findAllByUserIdOrderByCreatedDesc(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> readAll(int from, int size, int userId) {

        if (iUserService.getUser(userId).isEmpty()) {
            log.error("NotFoundException: {}", "При обновлении пользователя, Пользователь с ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }

        Pageable pageable = PageRequest.of(from / size, size);

        List<Request> pagedList = iRequestRepository.getPagedRequests(userId, pageable);

        return pagedList.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());

    }

    @Override
    public RequestDto read(int requestId, int userId) {

        Optional<Request> request = iRequestRepository.findById(requestId);

        if (request.isEmpty()) {
            log.error("NotFoundException: {}", "запрос c ИД " + requestId + " не найден");
            throw new NotFoundException("запрос c ИД " + requestId + " не найден");
        }

        Optional<User> user = iUserService.getUser(userId);
        if (user.isEmpty()) {
            log.error("NotFoundException: {}", "Пользователь с ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }

        return RequestMapper.toRequestDto(request.get());
    }

    @Override
    public RequestDto create(RequestDto requestDto, int userId) {

        Optional<User> requester = iUserService.getUser(userId);

        if (requester.isEmpty()) {
            log.error("NotFoundException: {}", "Пользователь c ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь c ИД " + userId + " не найден");
        }

        return RequestMapper.toRequestDto(iRequestRepository.save(
                RequestMapper.createRequest(requestDto, requester.get())));
    }

    @Override
    public RequestDto update(RequestDto requestDto, int userId, int requestId) {

        Optional<Request> request = iRequestRepository.findById(requestId);

        if (request.isEmpty()) {
            log.error("NotFoundException: {}", "запрос c ИД " + requestId + " не найден");
            throw new NotFoundException("запрос c ИД " + requestId + " не найден");
        }

        Optional<User> requester = iUserService.getUser(userId);

        if (requester.isEmpty()) {
            log.error("NotFoundException: {}", "Пользователь c ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь c ИД " + userId + " не найден");
        }

        if (request.get().getUser().getId() != userId) {
            log.error("BadRequestException: {}", "Только автор запроса может его изменить");
            throw new BadRequestException("Только автор запроса может его изменить");
        }

        request.get().setDescription(requestDto.getDescription());

        return RequestMapper.toRequestDto(iRequestRepository.save(request.get()));
    }

    @Override
    public HttpStatus delete(int userId, int requestId) {

        Optional<Request> request = iRequestRepository.findById(requestId);

        if (request.isEmpty()) {
            log.error("NotFoundException: {}", "запрос c ИД " + requestId + " не найден");
            throw new NotFoundException("запрос c ИД " + requestId + " не найден");
        }

        Optional<User> requester = iUserService.getUser(userId);

        if (requester.isEmpty()) {
            log.error("NotFoundException: {}", "Пользователь c ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь c ИД " + userId + " не найден");
        }

        if (request.get().getUser().getId() != userId) {
            log.error("BadRequestException: {}", "Только автор запроса может его удалить");
            throw new BadRequestException("Только автор запроса может его удалить");
        }

        iRequestRepository.deleteById(requestId);

        return HttpStatus.OK;
    }
}
