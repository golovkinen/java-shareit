package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

public interface IBookingRepositoryCustom {
    List<Booking> getAllUserBookingsByDateState(int userId, String state);

    List<Booking> getAllOwnerItemsBookingsByDateState(int ownerId, String state);

    List<Booking> getAllItemBookingsWithWaitingStatus(int itemId);

    void checkTimeCrossing(CreateBookingDto createBookingDto);

}
