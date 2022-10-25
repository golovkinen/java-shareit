package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.IBookingRepository;
import ru.practicum.shareit.booking.repository.IBookingRepositoryCustom;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.request.repository.IRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookingServiceTests {


    IItemRepository iItemRepository;
    IUserService iUserService;
    IRequestRepository iRequestRepository;
    BookingService bookingService;

    IBookingRepository iBookingRepository;

    IBookingRepositoryCustom iBookingRepositoryCustom;

    User user1;

    User user2;

    Item item1;

    Item item2;

    Comment comment1;

    Booking booking1;

    @BeforeEach
    void beforeEach() {

        iUserService = mock(IUserService.class);
        iRequestRepository = mock(IRequestRepository.class);
        iBookingRepository = mock(IBookingRepository.class);
        iBookingRepositoryCustom = mock(IBookingRepositoryCustom.class);
        iItemRepository = mock(IItemRepository.class);

        bookingService = new BookingService(iUserService, iItemRepository, iBookingRepository, iBookingRepositoryCustom);

        user1 = new User(1, "Email1@mail.com", "Name1", Collections.singletonList(item1), new HashSet<>(), new HashSet<>(), new HashSet<>());
        user2 = new User(2, "Email2@mail.com", "Name2", Collections.singletonList(item2), Collections.singleton(booking1), new HashSet<>(), Collections.singleton(comment1));
        item1 = new Item(1, "Item Name1", "Item1 Desc", true, user1, Collections.singleton(booking1), Collections.singleton(comment1), null);
        comment1 = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
                item1, user2);
        item2 = new Item(2, "Item Name2", "Item2 Desc", true, user2, new HashSet<>(), new HashSet<>(), null);
        booking1 = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
                LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item1, user2);


    }

    @Test
    void getAllUserBookings() {

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user2));

        when(iBookingRepositoryCustom.findBookingsByUserId(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking1));

        final List<BookingInfoDto> list = bookingService.readAllUserBookings(1, "ALL", 0, 10);

        assertNotNull(list);
        assertEquals(1, list.get(0).getId());
        assertEquals(1, list.size());

    }

    @Test
    void getBookingById() {

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user2));

        when(iBookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking1));

        final BookingInfoDto list = bookingService.read(1, 2);

        assertNotNull(list);
        assertEquals(1, list.getId());

    }

    @Test
    void getBookingListOfAllUserItems() {

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user1));

        when(iBookingRepositoryCustom.findAllItemOwnerBookings(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking1));

        final List<BookingInfoDto> list = bookingService.readBookingListOfAllUserItems(1, "ALL", 0, 10);

        assertNotNull(list);
        assertEquals(1, list.get(0).getId());
        assertEquals(1, list.size());

    }

    @Test
    void createBooking() {
        LocalDateTime start = LocalDateTime.now().plusMinutes(5).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(10).withNano(0);

        CreateBookingDto createBookingDto = new CreateBookingDto(2, start, end);

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user1));

        when(iItemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item2));

        when(iBookingRepository.save(any()))
                .thenReturn(booking1);

        final BookingInfoDto newItem = bookingService.create(1, createBookingDto);

        assertNotNull(newItem);
        assertEquals(1, newItem.getId());

    }

    @Test
    void approveBooking() {

        Booking bookingApproved = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
                LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.APPROVED, item1, user2);


        when(iBookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking1));

        when(iBookingRepository.save(any()))
                .thenReturn(bookingApproved);

        final BookingInfoDto newItem = bookingService.bookingApproval("true", 1, 1);

        assertNotNull(newItem);
        assertEquals(1, newItem.getId());
        assertEquals(Status.APPROVED, newItem.getStatus());

    }
}
