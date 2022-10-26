package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllByUserId(int userId);

    @Query("select i from Item i order by i.id asc")
    List<Item> findAllPaged(Pageable pageable);

    @Query("select b from Booking b where b.item.id = :itemId and b.user.id = :authorId and b.status = 'APPROVED' and b.endDate < :dateNow ORDER BY b.startDate desc")
    List<Booking> checkUserBookedItemBeforeComment(
            @Param(value = "itemId") int itemId,
            @Param(value = "authorId") int authorId,
            @Param(value = "dateNow") LocalDateTime dateNow);

    @Query("select i from Item i where i.user.id = :userId order by i.id asc")
    List<Item> readAllUserItemsByUserIdPaged(
            @Param(value = "userId") int itemId,
            Pageable pageable);

    @Query(" select i from Item i where i.available = true and (upper(i.name) like upper(concat('%', ?1, '%')) or upper(i.description) like upper(concat('%', ?1, '%'))) order by i.id asc")
    List<Item> searchItemByWord(String text, Pageable pageable);

    @Query("select b from Booking b where b.item.id = :itemId and b.endDate < :dateNow ORDER BY b.endDate desc")
    Optional<Booking> getItemsLastBooking(
            @Param(value = "itemId") int itemId,
            @Param(value = "dateNow") LocalDateTime dateNow);

    @Query("select b from Booking b where b.item.id = :itemId and b.startDate > :dateNow ORDER BY b.startDate asc")
    Optional<Booking> getItemsNextBooking(
            @Param(value = "itemId") int itemId,
            @Param(value = "dateNow") LocalDateTime dateNow);

}
