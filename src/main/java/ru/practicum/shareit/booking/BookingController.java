package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.IBookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {

    private final IBookingService iBookingService;

    public BookingController(IBookingService iBookingService) {
        this.iBookingService = iBookingService;
    }

    @GetMapping
    public ResponseEntity<List<BookingInfoDto>> readAllUserBookings(@RequestParam(defaultValue = "ALL") String state,
                                                                    @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        final Optional<List<BookingInfoDto>> bookings = iBookingService.readAllUserBookings(userId, state);
        return !bookings.isEmpty()
                ? new ResponseEntity<>(bookings.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<BookingInfoDto> read(@PathVariable(name = "bookingId") int bookingId,
                                               @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        final Optional<BookingInfoDto> bookingDto = iBookingService.read(bookingId, userId);

        return !bookingDto.isEmpty()
                ? new ResponseEntity<>(bookingDto.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "/owner")
    public ResponseEntity<List<BookingInfoDto>> readBookingListOfAllUserItems(@RequestParam(defaultValue = "ALL") String state,
                                                                              @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        final Optional<List<BookingInfoDto>> bookings = iBookingService.readBookingListOfAllUserItems(userId, state);
        return !bookings.isEmpty()
                ? new ResponseEntity<>(bookings.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<BookingInfoDto> create(@Valid @RequestBody CreateBookingDto createBookingDto,
                                                 @RequestHeader(name = "X-Sharer-User-Id") int userId) {

        Optional<BookingInfoDto> newBooking = iBookingService.create(userId, createBookingDto);
        log.debug(String.valueOf(newBooking));

        return !newBooking.isEmpty()
                ? new ResponseEntity<>(newBooking.get(), HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<BookingInfoDto> bookingApproval(@PathVariable(name = "bookingId") int bookingId,
                                                          @RequestParam String approved,
                                                          @RequestHeader(name = "X-Sharer-User-Id") int userId) {

        final boolean updated = iBookingService.bookingApproval(approved, bookingId, userId);

        return updated
                ? new ResponseEntity<>(iBookingService.getBookingAfterApproved(bookingId), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
