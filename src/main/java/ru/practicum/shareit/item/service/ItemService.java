package ru.practicum.shareit.item.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptionhandler.BadRequestException;
import ru.practicum.shareit.exceptionhandler.NotFoundException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService implements IItemService {

    private final IItemRepository iItemRepository;
    private final IItemRepositoryCustom iItemRepositoryCustom;
    private final ICommentRepository iCommentRepository;
    private final IUserService iUserService;

    public ItemService(IItemRepository iItemRepository, IItemRepositoryCustom iItemRepositoryCustom, ICommentRepository iCommentRepository, IUserService iUserService) {
        this.iItemRepository = iItemRepository;
        this.iItemRepositoryCustom = iItemRepositoryCustom;
        this.iCommentRepository = iCommentRepository;
        this.iUserService = iUserService;
    }

    @Override
    public ItemInfoDto create(ItemDto itemDto, int userId) {
        Optional<User> user = iUserService.getUser(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }
        Item item = ItemMapper.toItem(itemDto, user.get());

        Item newItem = iItemRepository.save(item);
        user.get().getItems().add(newItem);
        return ItemMapper.toItemDto(newItem,
                Optional.empty(),
                Optional.empty());
    }

    @Override
    public List<ItemInfoDto> readAll() {
        List<Item> itemsList = iItemRepository.findAll();
        if (itemsList.isEmpty()) {
            throw new NotFoundException("Совсем нет вещей");
        }
        return itemsList.stream().map(f -> ItemMapper.toItemDto(f,
                        Optional.empty(),
                        Optional.empty()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemInfoDto> readAllUserItems(int userId) {
        Optional<User> user = iUserService.getUser(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }
        return user.get().getItems().stream().map(f -> ItemMapper.toItemDto(f,
                        iItemRepositoryCustom.getItemsLastBooking(f.getId()),
                        iItemRepositoryCustom.getItemsNextBooking(f.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemInfoDto read(int id, int userId) {
        Optional<Item> item = iItemRepository.findById(id);
        if (item.isEmpty()) {
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
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }

        if (itemToUpdate.isEmpty()) {
            throw new NotFoundException("Вещь с ИД " + itemId + " не найдена");
        }

        if (itemToUpdate.get().getUser().getId() != userId) {
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
            throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
        }

        Optional<Item> itemToDelete = iItemRepository.findById(itemId);

        if (itemToDelete.isEmpty()) {
            throw new NotFoundException("Вещь с ИД " + itemId + " не найдена");
        }
        if (itemToDelete.get().getUser().getId() != userId) {
            throw new BadRequestException("Только собственник может удалить свою вещь");
        }
        iItemRepository.deleteById(itemId);
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus deleteAllUserItems(int id) {
        if (iUserService.getUser(id).isEmpty()) {
            throw new NotFoundException("Пользователь с ИД " + id + " не найден");
        }

        iItemRepository.deleteAllUserItems(id);
        return HttpStatus.OK;
    }

    @Override
    public List<ItemInfoDto> searchItemByWord(String searchSentence) {
        List<Item> searchResult = iItemRepositoryCustom.searchItemByWord(searchSentence);
        if (searchResult == null) {
            throw new NotFoundException("Ничего не найдено");
        }
        return searchResult.stream()
                .map(f -> ItemMapper.toItemDto(f,
                        iItemRepositoryCustom.getItemsLastBooking(f.getId()),
                        iItemRepositoryCustom.getItemsNextBooking(f.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public CommentInfoDto createComment(CommentCreateDto commentCreateDto, Integer itemId, Integer authorId) {
        Optional<User> author = iUserService.getUser(authorId);
        Optional<Item> item = iItemRepository.findById(itemId);
        if (author.isEmpty()) {
            throw new NotFoundException("Пользователь с ИД " + authorId + " не найден");
        }

        if (item.isEmpty()) {
            throw new NotFoundException("Вещь с ИД " + itemId + " не найдена");
        }

        if (author.get().getBookings().isEmpty()) {
            throw new BadRequestException("Пользователь с ИД " + authorId + " не бронировал ничего");
        }

        if (iItemRepository.checkUserBookedItemBeforeComment(itemId, authorId, LocalDateTime.now()).isEmpty()) {
            throw new BadRequestException("Нельзя комментировать пока не прошла аренда");
        }

        Comment newComment = iCommentRepository.save(CommentMapper.toComment(commentCreateDto, author.get(), item.get()));

        item.get().getComments().add(newComment);

        return CommentMapper.toCommentDto(newComment);
    }
}
