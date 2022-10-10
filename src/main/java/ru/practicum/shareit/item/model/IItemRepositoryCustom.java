package ru.practicum.shareit.item.model;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

public interface IItemRepositoryCustom {
    boolean updateItem(Item item, int userId, int itemId);

    boolean deleteItem(int itemId, int userId);

    List<Item> searchItemByWord(String searchSentence);

    Optional<Booking> getItemsLastBooking(int itemId);

    Optional<Booking> getItemsNextBooking(int itemId);

    Optional<List<Booking>> checkUserBookedItemBeforeComment(int itemId, int authorId);
}
