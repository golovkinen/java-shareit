package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") int userId,
											  @RequestParam(name = "state", defaultValue = "all") String stateParam,
											  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
											  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") int userId,
										   @RequestBody @Valid CreateBookingDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") int userId,
											 @PathVariable int bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping(value = "/owner")
	public ResponseEntity<Object> readBookingListOfAllUserItems(@RequestParam(defaultValue = "ALL") String state,
																@RequestHeader(name = "X-Sharer-User-Id") int userId,
																@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
																@Valid @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
		BookingState stateParam = BookingState.from(state)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
		log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);

		return bookingClient.readBookingListOfAllUserItems(userId, stateParam, from, size);
	}

	@PatchMapping(value = "/{bookingId}")
	public ResponseEntity<Object> bookingApproval(@PathVariable(name = "bookingId") int bookingId,
												  @RequestParam String approved,
												  @RequestHeader(name = "X-Sharer-User-Id") int userId) {

		return bookingClient.bookingApproval(approved, bookingId, userId);
	}

	@DeleteMapping(value = "/{bookingId}")
	public ResponseEntity<Object> delete(@PathVariable(name = "bookingId") int bookingId,
										 @RequestHeader(name = "X-Sharer-User-Id") int userId) {
		return bookingClient.delete(bookingId, userId);
	}

}
