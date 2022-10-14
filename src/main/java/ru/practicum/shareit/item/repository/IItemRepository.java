package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface IItemRepository extends JpaRepository<Item, Integer> {

    @Query("DELETE FROM Item item WHERE item.user = :id")
    void deleteAllUserItems(@Param(value = "id") int id);

    @Query("select b from Booking b where b.item.id = :itemId and b.user.id = :authorId and b.status = 'APPROVED' and b.endDate < :dateNow ORDER BY b.startDate desc")
    List<Booking> checkUserBookedItemBeforeComment(
            @Param(value = "itemId") int itemId,
            @Param(value = "authorId") int authorId,
            @Param(value = "dateNow") LocalDateTime dateNow);

}
