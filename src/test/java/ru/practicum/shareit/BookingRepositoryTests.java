package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepositoryCustom;
import ru.practicum.shareit.booking.repository.IBookingRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ICommentRepository;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.IUserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = "/test_schema.sql")
public class BookingRepositoryTests {

    @Autowired
    IItemRepository iItemRepository;
    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    ICommentRepository iCommentRepository;
    BookingRepositoryCustom bookingRepositoryCustom;

    @Autowired
    EntityManager entityManager;
    @Autowired
    IBookingRepository iBookingRepository;

    User user1;
    User user2;
    Item item1;
    Item item2;
    Comment comment1;
    Booking booking1;
    Booking booking2;
    Booking booking3;

    @BeforeEach
    void beforeEach() {

        bookingRepositoryCustom = new BookingRepositoryCustom(entityManager);

        user1 = iUserRepository.save(new User(null, "Email1@mail.com", "Name1", Collections.singletonList(item1), new HashSet<>(), new HashSet<>(), new HashSet<>()));
        user2 = iUserRepository.save(new User(null, "Email2@mail.com", "Name2", Collections.singletonList(item2), Collections.singleton(booking1), new HashSet<>(), Collections.singleton(comment1)));
        item1 = iItemRepository.save(new Item(null, "Item Name1", "Item1 Desc", true, user1, Collections.singleton(booking1), Collections.singleton(comment1), null));
        comment1 = iCommentRepository.save(new Comment(null, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
                item1, user2));
        item2 = iItemRepository.save(new Item(null, "Item Name2", "Item2 Desc", true, user2, new HashSet<>(), new HashSet<>(), null));
        booking1 = iBookingRepository.save(new Booking(null, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
                LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.APPROVED, item1, user2));
        booking2 = iBookingRepository.save(new Booking(null, LocalDateTime.of(2022, 11, 1, 10, 00, 00),
                LocalDateTime.of(2022, 11, 2, 10, 00, 00), Status.WAITING, item1, user2));
        booking3 = iBookingRepository.save(new Booking(null, LocalDateTime.of(2022, 12, 1, 10, 00, 00),
                LocalDateTime.of(2022, 12, 2, 10, 00, 00), Status.REJECTED, item1, user2));
    }

    @Test
    void getAllUserBookings() {
        List<Booking> list = bookingRepositoryCustom.findBookingsByUserId(2, 0, 10);

        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(3, list.get(0).getBookingId());
    }

    @Test
    void getAllUserBookingsPast() {
        List<Booking> list = bookingRepositoryCustom.findAllPastUserBookings(2, 0, 10);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getBookingId());
    }

    @Test
    void getAllUserBookingsFuture() {
        List<Booking> list = bookingRepositoryCustom.findAllFutureUserBookings(2, 0, 10);

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(3, list.get(0).getBookingId());
    }

    @Test
    void getAllUserBookingsCurrent() {
        List<Booking> list = bookingRepositoryCustom.findAllCurrentUserBookings(2, 0, 10);

        assertTrue(list.isEmpty());
    }

    @Test
    void getAllUserBookingsWaiting() {
        List<Booking> list = bookingRepositoryCustom.findAllUserBookingsByStatus(Status.WAITING, 2, 0, 10);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(2, list.get(0).getBookingId());
    }

    @Test
    void getAllUserBookingsApproved() {
        List<Booking> list = bookingRepositoryCustom.findAllUserBookingsByStatus(Status.APPROVED, 2, 0, 10);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getBookingId());
    }

    @Test
    void getAllUserBookingsRejected() {
        List<Booking> list = bookingRepositoryCustom.findAllUserBookingsByStatus(Status.REJECTED, 2, 0, 10);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(3, list.get(0).getBookingId());
    }

    @Test
    void getBookingById() {
        Optional<Booking> list = iBookingRepository.findById(1);

        assertTrue(list.isPresent());
        assertEquals(1, list.get().getBookingId());
    }

    @Test
    void getAllOwnerBookings() {
        List<Booking> list = bookingRepositoryCustom.findAllItemOwnerBookings(1, 0, 10);

        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(3, list.get(0).getBookingId());
    }

    @Test
    void getAllOwnerBookingsPast() {
        List<Booking> list = bookingRepositoryCustom.findAllItemOwnerPastBookings(1, 0, 10);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getBookingId());
    }

    @Test
    void getAllOwnerBookingsFuture() {
        List<Booking> list = bookingRepositoryCustom.findAllItemOwnerFutureBookings(1, 0, 10);

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(3, list.get(0).getBookingId());
    }

    @Test
    void getAllOwnerBookingsCurrent() {
        List<Booking> list = bookingRepositoryCustom.findAllItemOwnerCurrentBookings(1, 0, 10);

        assertTrue(list.isEmpty());
    }

    @Test
    void getAllOwnerBookingsWaiting() {
        List<Booking> list = bookingRepositoryCustom.findAllItemOwnerBookingsByStatus(Status.WAITING, 1, 0, 10);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(2, list.get(0).getBookingId());
    }

    @Test
    void getAllOwnerBookingsApproved() {
        List<Booking> list = bookingRepositoryCustom.findAllItemOwnerBookingsByStatus(Status.APPROVED, 1, 0, 10);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getBookingId());
    }

    @Test
    void getAllOwnerBookingsRejected() {
        List<Booking> list = bookingRepositoryCustom.findAllItemOwnerBookingsByStatus(Status.REJECTED, 1, 0, 10);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(3, list.get(0).getBookingId());
    }


    @Test
    void createNew() {
        Booking list = iBookingRepository.save(new Booking(null, LocalDateTime.of(2023, 12, 1, 10, 00, 00),
                LocalDateTime.of(2023, 12, 2, 10, 00, 00), Status.WAITING, item1, user2));

        assertNotNull(list);
        assertEquals(4, list.getBookingId());
    }
}