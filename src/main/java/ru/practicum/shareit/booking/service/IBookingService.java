package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

public interface IBookingService {
    List<BookingInfoDto> readAllUserBookings(int userId, String state);

    BookingInfoDto read(int bookingId, int userId);

    List<BookingInfoDto> readBookingListOfAllUserItems(int userId, String state);

    BookingInfoDto create(int userId, CreateBookingDto bookingDto);

    BookingInfoDto bookingApproval(String status, int bookingId, int userId);

    boolean delete(int bookingId, int userId);

}
