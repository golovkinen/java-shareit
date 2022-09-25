package ru.practicum.shareit.item.model;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemRepositoryCustom implements IItemRepositoryCustom {

    @Autowired
    private EntityManager entityManager;
    private final IItemRepository iItemRepository;

    public ItemRepositoryCustom(IItemRepository iItemRepository) {
        this.iItemRepository = iItemRepository;
    }

    @Override
    public boolean updateItem(Item item, int userId, int itemId) {

        Optional<Item> itemToUpdate = iItemRepository.findById(itemId);

        if (!itemToUpdate.isEmpty() && itemToUpdate.get().getUser().getId() == userId) {

            if (item.getName() != null) {
                itemToUpdate.get().setName(item.getName());
            }
            if (item.getDescription() != null) {
                itemToUpdate.get().setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                itemToUpdate.get().setAvailable(item.getAvailable());
            }

            iItemRepository.save(itemToUpdate.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteItem(int itemId, int userId) {

        Optional<Item> itemToDelete = iItemRepository.findById(itemId);

        if (!itemToDelete.isEmpty() && itemToDelete.get().getUser().getId() == userId) {
            iItemRepository.deleteById(itemId);
            return true;
        }
        return false;
    }

    public List<Item> searchItemByWord(String searchSentence) {
        SearchSession searchSession = Search.session(entityManager);

        return searchSession.search(Item.class)
                .where(f -> f.bool()
                        .should(f.match()
                                .field("name")
                                .matching(searchSentence)
                                .fuzzy())
                        .must(f.match()
                                .field("description")
                                .matching(searchSentence)
                                .fuzzy())
                        .must(f.match()
                                .field("available")
                                .matching(true))
                )
                .fetchHits(20);
    }

}
