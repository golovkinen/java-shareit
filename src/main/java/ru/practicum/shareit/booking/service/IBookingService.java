package ru.practicum.shareit.booking.service;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

public interface IBookingService {
    List<BookingInfoDto> readAllUserBookings(int userId, String state, int from, int size);

    BookingInfoDto read(int bookingId, int userId);

    List<BookingInfoDto> readBookingListOfAllUserItems(int userId, String state, int from, int size);

    BookingInfoDto create(int userId, CreateBookingDto bookingDto);

    BookingInfoDto bookingApproval(String status, int bookingId, int userId);

    HttpStatus delete(int bookingId, int userId);

}
