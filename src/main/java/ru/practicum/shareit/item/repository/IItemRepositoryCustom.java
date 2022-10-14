package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface IItemRepositoryCustom {

    List<Item> searchItemByWord(String searchSentence);

    Optional<Booking> getItemsLastBooking(int itemId);

    Optional<Booking> getItemsNextBooking(int itemId);

}
