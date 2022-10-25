package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptionhandler.BadRequestException;
import ru.practicum.shareit.exceptionhandler.NotFoundException;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ICommentRepository;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.item.repository.IItemRepositoryCustom;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.IRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.IUserRepository;
import ru.practicum.shareit.user.service.IUserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService implements IItemService {

    private final IItemRepository iItemRepository;
    private final IItemRepositoryCustom iItemRepositoryCustom;
    private final ICommentRepository iCommentRepository;
    private final IUserService iUserService;
    private final IUserRepository iUserRepository;
    private final IRequestRepository iRequestRepository;

    public ItemService(IItemRepository iItemRepository, IItemRepositoryCustom iItemRepositoryCustom, ICommentRepository iCommentRepository, IUserService iUserService, IUserRepository iUserRepository, IRequestRepository iRequestRepository) {
        this.iItemRepository = iItemRepository;
        this.iItemRepositoryCustom = iItemRepositoryCustom;
        this.iCommentRepository = iCommentRepository;
        this.iUserService = iUserService;
        this.iUserRepository = iUserRepository;
        this.iRequestRepository = iRequestRepository;
    }

    @Override
    public ItemInfoDto create(ItemDto itemDto, int userId) {
        Optional<User> user = iUserService.getUser(userId);
        if (user.isEmpty()) {
            log.error("NotFoundException: {}", "При создании вещи, Пользователь с ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }

        Item item = ItemMapper.toItem(itemDto, user.get());

        if (itemDto.getRequestId() != null) {

            Optional<Request> request = iRequestRepository.findById(itemDto.getRequestId());

            if (request.isEmpty()) {
                log.error("NotFoundException: {}", "Запрос с ИД " + itemDto.getRequestId() + " не найден");
                throw new NotFoundException("Запрос с ИД " + itemDto.getRequestId() + " не найден");
            }

            item.setRequest(request.get());
            request.get().getRequestResponses().add(item);
            iRequestRepository.save(request.get());
        }

        Item newItem = iItemRepository.save(item);

        user.get().getItems().add(newItem);
        iUserRepository.save(user.get());
        return ItemMapper.toItemDto(newItem,
                Optional.empty(),
                Optional.empty());
    }

    @Override
    public List<ItemInfoDto> readAll(int from, int size) {

        List<Item> itemsList = iItemRepositoryCustom.readAllItemsPaged(from, size);

        if (itemsList.isEmpty()) {
            throw new NotFoundException("Совсем нет вещей");
        }
        return itemsList.stream().map(f -> ItemMapper.toItemDto(f,
                        Optional.empty(),
                        Optional.empty()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemInfoDto> readAllUserItems(int userId, int from, int size) {
        Optional<User> user = iUserService.getUser(userId);
        if (user.isEmpty()) {
            log.error("NotFoundException: {}", "При чтении всех вещей, Пользователь с ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }

        if (user.get().getItems().isEmpty()) {
            log.error("NotFoundException: {}", "При чтении всех вещей, у пользователя с ИД " + userId + " вещей не найдено");
            throw new NotFoundException("У пользователя с ИД " + userId + " нет вещей");
        }

        return iItemRepositoryCustom.readAllUserItemsByUserIdPaged(userId, from, size).stream()
                .map(f -> ItemMapper.toItemDto(f,
                        iItemRepositoryCustom.getItemsLastBooking(f.getId()),
                        iItemRepositoryCustom.getItemsNextBooking(f.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemInfoDto read(int id, int userId) {
        Optional<Item> item = iItemRepository.findById(id);
        if (item.isEmpty()) {
            log.error("NotFoundException: {}", "При чтении из БД, Вещь с ИД " + id + " не найдена");
            throw new NotFoundException("Вещь с ИД " + id + " не найдена");
        }

        if (item.get().getUser().getId() == userId) {
            return ItemMapper.toItemDto(item.get(),
                    iItemRepositoryCustom.getItemsLastBooking(item.get().getId()),
                    iItemRepositoryCustom.getItemsNextBooking(item.get().getId()));
        }
        return ItemMapper.toItemDto(item.get(),
                Optional.empty(),
                Optional.empty());
    }

    @Override
    public ItemInfoDto update(ItemDto itemDto, int userId, int itemId) {

        Optional<Item> itemToUpdate = iItemRepository.findById(itemId);

        if (iUserService.getUser(userId).isEmpty()) {
            log.error("NotFoundException: {}", "При обновлении Пользователь с ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }

        if (itemToUpdate.isEmpty()) {
            log.error("NotFoundException: {}", "При обновлении Вещь с ИД " + itemId + " не найдена");
            throw new NotFoundException("Вещь с ИД " + itemId + " не найдена");
        }

        if (itemToUpdate.get().getUser().getId() != userId) {
            log.error("NotFoundException: {}", "Обновлять может только собственник вещи");
            throw new NotFoundException("Обновлять может только собственник вещи");
        }
        if (itemDto.getName() != null) {
            itemToUpdate.get().setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemToUpdate.get().setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemToUpdate.get().setAvailable(itemDto.getAvailable());
        }
        iItemRepository.save(itemToUpdate.get());
        return read(itemId, userId);
    }

    @Override
    public HttpStatus delete(int itemId, int userId) {
        if (iUserService.getUser(userId).isEmpty()) {
            log.error("NotFoundException: {}", "При удалении вещи, Пользователь с ИД " + userId + " не найден");
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }

        Optional<Item> itemToDelete = iItemRepository.findById(itemId);

        if (itemToDelete.isEmpty()) {
            log.error("NotFoundException: {}", "При удалении вещи, Вещь с ИД " + itemId + " не найдена");
            throw new NotFoundException("Вещь с ИД " + itemId + " не найдена");
        }
        if (itemToDelete.get().getUser().getId() != userId) {
            log.error("BadRequestException: {}", "При удалении вещи, Только собственник может удалить свою вещь");
            throw new BadRequestException("Только собственник может удалить свою вещь");
        }
        iItemRepository.deleteById(itemId);
        return HttpStatus.OK;
    }

    public HttpStatus deleteAllUserItems(int id) {
        if (this.iUserService.getUser(id).isEmpty()) {
            log.error("NotFoundException: {}", "При удалении всех вещей пользователя, Пользователь с ИД " + id + " не найден");
            throw new NotFoundException("Пользователь с ИД " + id + " не найден");
        } else {
            this.iItemRepository.findAllByUserId(id).stream().forEach((f) -> {
                this.iItemRepository.deleteById(f.getId());
            });
            return HttpStatus.OK;
        }
    }

    @Override
    public List<ItemInfoDto> searchItemByWord(String searchSentence, int from, int size) {

        List<Item> searchResult = iItemRepositoryCustom.searchItemByWord(searchSentence, from, size);

        return searchResult.stream()
                .map(f -> ItemMapper.toItemDto(f,
                        iItemRepositoryCustom.getItemsLastBooking(f.getId()),
                        iItemRepositoryCustom.getItemsNextBooking(f.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public CommentInfoDto createComment(CommentInfoDto commentInfoDto, Integer itemId, Integer authorId) {
        Optional<User> author = iUserService.getUser(authorId);
        Optional<Item> item = iItemRepository.findById(itemId);
        if (author.isEmpty()) {
            log.error("NotFoundException: {}", "При создании комментария Пользователь с ИД " + authorId + " не найден");
            throw new NotFoundException("Пользователь с ИД " + authorId + " не найден");
        }

        if (item.isEmpty()) {
            log.error("NotFoundException: {}", "При создании комментария Вещь с ИД " + itemId + " не найдена");
            throw new NotFoundException("Вещь с ИД " + itemId + " не найдена");
        }

        if (author.get().getBookings().isEmpty()) {
            log.error("BadRequestException: {}", "При создании комментария Пользователь с ИД " + authorId + " не бронировал ничего");
            throw new BadRequestException("Пользователь с ИД " + authorId + " не бронировал ничего");
        }

        if (iItemRepository.checkUserBookedItemBeforeComment(itemId, authorId, LocalDateTime.now()).isEmpty()) {
            log.error("BadRequestException: {}", "Нельзя комментировать пока не прошла аренда");
            throw new BadRequestException("Нельзя комментировать пока не прошла аренда");
        }

        Comment newComment = iCommentRepository.save(CommentMapper.toComment(commentInfoDto, author.get(), item.get()));

        item.get().getComments().add(newComment);

        return CommentMapper.toCommentDto(newComment);
    }
}
