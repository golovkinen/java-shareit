package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface IBookingRepositoryCustom {

    List<Booking> findAllUserBookingsByStatus(Status status, int userId, int from, int size);

    List<Booking> findAllItemOwnerBookingsByStatus(Status status, int ownerId, int from, int size);

    List<Booking> findBookingsByUserId(int userId, int from, int size);

    List<Booking> findAllCurrentUserBookings(int userId, int from, int size);

    List<Booking> findAllPastUserBookings(int userId, int from, int size);

    List<Booking> findAllFutureUserBookings(int userId, int from, int size);

    List<Booking> findAllItemOwnerBookings(int ownerId, int from, int size);

    List<Booking> findAllItemOwnerCurrentBookings(int ownerId, int from, int size);

    List<Booking> findAllItemOwnerPastBookings(int ownerId, int from, int size);

    List<Booking> findAllItemOwnerFutureBookings(int ownerId, int from, int size);
}
