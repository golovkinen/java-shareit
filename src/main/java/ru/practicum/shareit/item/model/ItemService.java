package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.user.model.IUserService;
import ru.practicum.shareit.user.model.User;

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
            return null;
        }
        Item item = ItemMapper.toItem(itemDto, user.get());

        Item newItem = iItemRepository.save(item);
        user.get().getItems().add(newItem);
        return ItemMapper.toItemDto(newItem,
                iItemRepositoryCustom.getItemsLastBooking(newItem.getId()),
                iItemRepositoryCustom.getItemsNextBooking(newItem.getId()));
    }

    @Override
    public Optional<List<ItemInfoDto>> readAll() {
        List<Item> itemsList = iItemRepository.findAll();
        if (itemsList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(iItemRepository.findAll().stream().map(f -> ItemMapper.toItemDto(f,
                        iItemRepositoryCustom.getItemsLastBooking(f.getId()),
                        iItemRepositoryCustom.getItemsNextBooking(f.getId())))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<ItemInfoDto>> readAllUserItems(int id) {
        Optional<User> user = iUserService.getUser(id);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(user.get().getItems().stream().map(f -> ItemMapper.toItemDto(f,
                        iItemRepositoryCustom.getItemsLastBooking(f.getId()),
                        iItemRepositoryCustom.getItemsNextBooking(f.getId())))
                .collect(Collectors.toList()));
    }

    @Override
    public ItemInfoDto read(int id, int userId) {
        Optional<Item> item = iItemRepository.findById(id);
        if (item.isEmpty()) {
            return null;
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
    public boolean update(ItemDto itemDto, int userId, int itemId) {
        Optional<User> user = iUserService.getUser(userId);
        if (user.isEmpty()) {
            return false;
        }
        return iItemRepositoryCustom.updateItem(ItemMapper.toItem(itemDto, user.get()), userId, itemId);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (iUserService.read(id).isEmpty()) {
            return false;
        }
        return iItemRepositoryCustom.deleteItem(id, userId);
    }

    @Override
    public boolean deleteAllUserItems(int id) {
        if (iUserService.read(id).isEmpty()) {
            return false;
        }

        iItemRepository.deleteAllUserItems(id);
        return true;
    }

    @Override
    public Optional<List<ItemInfoDto>> searchItemByWord(String searchSentence) {
        List<Item> searchResult = iItemRepositoryCustom.searchItemByWord(searchSentence);
        if (searchResult == null) {
            return Optional.empty();
        }
        return Optional.of(searchResult.stream()
                .map(f -> ItemMapper.toItemDto(f,
                        iItemRepositoryCustom.getItemsLastBooking(f.getId()),
                        iItemRepositoryCustom.getItemsNextBooking(f.getId())))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<CommentInfoDto> createComment(CommentCreateDto commentCreateDto, Integer itemId, Integer authorId) {
        Optional<User> author = iUserService.getUser(authorId);
        Optional<Item> item = iItemRepository.findById(itemId);
        if (author.isEmpty() || item.isEmpty()) {
            return Optional.empty();
        }

        if (iItemRepositoryCustom.checkUserBookedItemBeforeComment(itemId, authorId).isEmpty()) {
            return Optional.empty();
        }

        Comment newComment = iCommentRepository.save(CommentMapper.toComment(commentCreateDto, author.get(), item.get()));

        item.get().getComments().add(newComment);

        return Optional.of(CommentMapper.toCommentDto(newComment));
    }
}
