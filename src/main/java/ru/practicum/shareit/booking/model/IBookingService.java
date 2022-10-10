package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;
import java.util.Optional;

public interface IBookingService {
    Optional<List<BookingInfoDto>> readAllUserBookings(int userId, String state);

    Optional<BookingInfoDto> read(int bookingId, int userId);

    Optional<List<BookingInfoDto>> readBookingListOfAllUserItems(int userId, String state);

    Optional<BookingInfoDto> create(int userId, CreateBookingDto bookingDto);

    boolean bookingApproval(String status, int bookingId, int userId);

    boolean delete(int bookingId, int userId);

    BookingInfoDto getBookingAfterApproved(int bookingId);
}
