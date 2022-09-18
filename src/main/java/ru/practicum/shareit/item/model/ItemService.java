package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.IUserService;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

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
    public Item create(Item item, int userId) {
        Optional<User> user = iUserService.read(userId);
        if (user.isEmpty()) {
            return null;
        }
        item.setUser(user.get());
        Item newItem = iItemRepository.save(item);
        user.get().getItems().add(newItem);
        return newItem;
    }

    @Override
    public List<Item> readAll() {
        return iItemRepository.findAll();
    }

    @Override
    public List<Item> readAllUserItems(int id) {
        Optional<User> user = iUserService.read(id);
        if (user.isEmpty()) {
            return null;
        }
        return user.get().getItems();
    }

    @Override
    public Optional<Item> read(int id) {
        return iItemRepository.findById(id);
    }

    @Override
    public boolean update(Item item, int userId, int itemId) {
        return iItemRepositoryCustom.updateItem(item, userId, itemId);
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
    public List<Item> searchItemByWord(String searchSentence) {
        return iItemRepositoryCustom.searchItemByWord(searchSentence);
    }
}
