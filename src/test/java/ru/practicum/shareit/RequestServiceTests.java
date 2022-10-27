package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.IRequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestServiceTests {

    IUserService iUserService;
    RequestService requestService;
    IRequestRepository iRequestRepository;

    User user1;
    User user2;
    Item item1;
    Item item2;
    Comment comment1;
    Booking booking1;
    Request request1;
    RequestDto requestDto;

    @BeforeEach
    void beforeEach() {

        iUserService = mock(IUserService.class);
        iRequestRepository = mock(IRequestRepository.class);

        requestService = new RequestService(iUserService, iRequestRepository);

        user1 = new User(1, "Email1@mail.com", "Name1", Collections.singletonList(item1), new HashSet<>(), new HashSet<>(), new HashSet<>());
        user2 = new User(2, "Email2@mail.com", "Name2", Collections.singletonList(item2), Collections.singleton(booking1), new HashSet<>(), Collections.singleton(comment1));
        item1 = new Item(1, "Item Name1", "Item1 Desc", true, user1, Collections.singleton(booking1), Collections.singleton(comment1), null);
        comment1 = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
                item1, user2);
        item2 = new Item(2, "Item Name2", "Item2 Desc", true, user2, new HashSet<>(), new HashSet<>(), null);
        booking1 = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
                LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.WAITING, item1, user2);
        requestDto = new RequestDto(1, "Request Desc", LocalDateTime.of(2022, 10, 2, 10, 00, 00), 1, new ArrayList<>());
        request1 = new Request(1, "Request Desc", LocalDateTime.of(2022, 10, 2, 10, 00, 00), user1, new HashSet<>());


    }

    @Test
    void getAllUserRequests() {

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user1));

        when(iRequestRepository.findAllByUserIdOrderByCreatedDesc(anyInt()))
                .thenReturn(Collections.singletonList(request1));

        final List<RequestDto> list = requestService.readAllUserRequests(1);

        assertNotNull(list);
        assertEquals(1, list.get(0).getId());
        assertEquals(1, list.size());

    }

    @Test
    void getAllRequestsPaged() {

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user1));

        when(iRequestRepository.getPagedRequests(anyInt(), any()))
                .thenReturn(Collections.singletonList(request1));

        final List<RequestDto> list = requestService.readAll(0, 10, 1);

        assertNotNull(list);
        assertEquals(1, list.get(0).getId());

    }

    @Test
    void getRequestById() {

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user1));

        when(iRequestRepository.findById(anyInt()))
                .thenReturn(Optional.of(request1));


        final RequestDto list = requestService.read(1, 1);

        assertNotNull(list);
        assertEquals(1, list.getId());

    }

    @Test
    void createRequest() {

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user1));

        when(iRequestRepository.save(any()))
                .thenReturn(request1);

        final RequestDto newItem = requestService.create(requestDto, 1);

        assertNotNull(newItem);
        assertEquals(1, newItem.getId());
    }

}
