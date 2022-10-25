package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.IBookingRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ICommentRepository;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.IRequestRepository;
import ru.practicum.shareit.request.repository.RequestRepositoryCustom;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.IUserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Sql(scripts = "/test_schema.sql")
public class RequestRepositoryTests {

    @Autowired
    IItemRepository iItemRepository;
    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    ICommentRepository iCommentRepository;

    @Autowired
    IBookingRepository iBookingRepository;

    RequestRepositoryCustom requestRepositoryCustom;

    @Autowired
    EntityManager entityManager;
    @Autowired
    IRequestRepository iRequestRepository;

    User user1;
    User user2;
    Item item1;
    Item item2;
    Comment comment1;
    Booking booking1;
    Booking booking2;
    Booking booking3;

    Request request1;

    Request request2;
    Request request3;

    @BeforeEach
    void beforeEach() {

        requestRepositoryCustom = new RequestRepositoryCustom(entityManager);

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
        request1 = iRequestRepository.save(new Request(null, "Request1 Desc", LocalDateTime.of(2022, 10, 2, 10, 00, 00), user1, new HashSet<>()));
        request2 = iRequestRepository.save(new Request(null, "Request2 Desc", LocalDateTime.of(2022, 11, 2, 10, 00, 00), user2, new HashSet<>()));
        request3 = iRequestRepository.save(new Request(null, "Request3 Desc", LocalDateTime.of(2022, 12, 2, 10, 00, 00), user1, new HashSet<>()));
    }

    @Test
    void getAllUserRequests() {
        List<Request> list = iRequestRepository.findAllByUserIdOrderByCreatedDesc(1);

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(3, list.get(0).getId());
    }

    @Test
    void getAllRequests() {
        List<Request> list = requestRepositoryCustom.getPagedRequests(0, 10, 2);

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0).getId());
    }

    @Test
    void getRequestById() {
        Optional<Request> list = iRequestRepository.findById(2);

        assertNotNull(list);
        assertEquals(2, list.get().getId());
    }

    @Test
    void createNew() {
        Request list = iRequestRepository.save(new Request(null, "Request4 Desc", LocalDateTime.of(2022, 12, 2, 10, 00, 00), user1, new HashSet<>()));

        assertNotNull(list);
        assertEquals(4, list.getId());
    }
}