package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.service.IBookingService;

import javax.validation.Valid;
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
                                                    @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return iBookingService.readAllUserBookings(userId, state);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingInfoDto read(@PathVariable(name = "bookingId") int bookingId,
                               @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return iBookingService.read(bookingId, userId);
    }


    @GetMapping(value = "/owner")
    public List<BookingInfoDto> readBookingListOfAllUserItems(@RequestParam(defaultValue = "ALL") String state,
                                                              @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return iBookingService.readBookingListOfAllUserItems(userId, state);
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
    public ResponseEntity<?> delete(@PathVariable(name = "bookingId") int bookingId,
                                    @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        final boolean deleted = iBookingService.delete(bookingId, userId);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
