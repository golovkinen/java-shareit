package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.service.IBookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {

    private final IBookingService iBookingService;

    public BookingController(IBookingService iBookingService) {
        this.iBookingService = iBookingService;
    }

    @GetMapping
    public List<BookingInfoDto> readAllUserBookings(@RequestParam(defaultValue = "ALL") String state,
                                                    @RequestHeader(name = "X-Sharer-User-Id") int userId,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return iBookingService.readAllUserBookings(userId, state, from, size);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingInfoDto read(@PathVariable(name = "bookingId") int bookingId,
                               @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return iBookingService.read(bookingId, userId);
    }


    @GetMapping(value = "/owner")
    public List<BookingInfoDto> readBookingListOfAllUserItems(@RequestParam(defaultValue = "ALL") String state,
                                                              @RequestHeader(name = "X-Sharer-User-Id") int userId,
                                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return iBookingService.readBookingListOfAllUserItems(userId, state, from, size);
    }

    @PostMapping
    public BookingInfoDto create(@Valid @RequestBody CreateBookingDto createBookingDto,
                                 @RequestHeader(name = "X-Sharer-User-Id") int userId) {

        return iBookingService.create(userId, createBookingDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingInfoDto bookingApproval(@PathVariable(name = "bookingId") int bookingId,
                                          @RequestParam String approved,
                                          @RequestHeader(name = "X-Sharer-User-Id") int userId) {

        return iBookingService.bookingApproval(approved, bookingId, userId);
    }

    @DeleteMapping(value = "/{bookingId}")
    public HttpStatus delete(@PathVariable(name = "bookingId") int bookingId,
                             @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return iBookingService.delete(bookingId, userId);
    }
}
