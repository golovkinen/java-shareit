package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.IUserService;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService implements IItemService {

    private final IItemRepository iItemRepository;
    private final IItemRepositoryCustom iItemRepositoryCustom;
    private final IUserService iUserService;

    public ItemService(IItemRepository iItemRepository, IItemRepositoryCustom iItemRepositoryCustom, IUserService iUserService) {
        this.iItemRepository = iItemRepository;
        this.iItemRepositoryCustom = iItemRepositoryCustom;
        this.iUserService = iUserService;
    }

    @Override
    public ItemDto create(ItemDto itemDto, int userId) {
        Optional<User> user = iUserService.getUser(userId);
        if (user.isEmpty()) {
            return null;
        }
        Item item = ItemMapper.toItem(itemDto, user.get());

        Item newItem = iItemRepository.save(item);
        user.get().getItems().add(newItem);
        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public Optional<List<ItemDto>> readAll() {
        List<Item> itemsList = iItemRepository.findAll();
        if (itemsList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(iItemRepository.findAll().stream().map(f -> ItemMapper.toItemDto(f))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<ItemDto>> readAllUserItems(int id) {
        Optional<User> user = iUserService.getUser(id);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(user.get().getItems().stream().map(f -> ItemMapper.toItemDto(f))
                .collect(Collectors.toList()));
    }

    @Override
    public ItemDto read(int id) {
        Optional<Item> item = iItemRepository.findById(id);
        if (item.isEmpty()) {
            return null;
        }
        return ItemMapper.toItemDto(item.get());
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
    public Optional<List<ItemDto>> searchItemByWord(String searchSentence) {
        List<Item> searchResult = iItemRepositoryCustom.searchItemByWord(searchSentence);
        if (searchResult == null) {
            return Optional.empty();
        }
        return Optional.of(searchResult.stream()
                .map(f -> ItemMapper.toItemDto(f)).collect(Collectors.toList()));
    }
}
