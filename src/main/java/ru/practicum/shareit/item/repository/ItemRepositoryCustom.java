package ru.practicum.shareit.item.repository;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemRepositoryCustom implements IItemRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

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
                .fetchHits(10);
    }

    @Override
    public Optional<Booking> getItemsLastBooking(int itemId) {
        try {
            return Optional.of((Booking) entityManager.createQuery("select b from Booking b where b.item.id = :itemId and b.endDate < :dateNow ORDER BY b.endDate desc")
                    .setParameter("itemId", itemId)
                    .setParameter("dateNow", LocalDateTime.now())
                    .setMaxResults(1)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Booking> getItemsNextBooking(int itemId) {
        try {
            return Optional.of((Booking) entityManager.createQuery("select b from Booking b where b.item.id = :itemId and b.startDate > :dateNow ORDER BY b.startDate asc")
                    .setParameter("itemId", itemId)
                    .setParameter("dateNow", LocalDateTime.now())
                    .setMaxResults(1)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
