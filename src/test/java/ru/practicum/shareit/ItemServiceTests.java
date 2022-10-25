package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ICommentRepository;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.item.repository.ItemRepositoryCustom;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.repository.IRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.IUserRepository;
import ru.practicum.shareit.user.service.IUserService;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemServiceTests {


    IItemRepository iItemRepository;
    ItemRepositoryCustom itemRepositoryCustom;
    ICommentRepository iCommentRepository;
    IUserService iUserService;
    IUserRepository iUserRepository;
    IRequestRepository iRequestRepository;
    ItemService itemService;

    @BeforeEach
    void beforeEach() {
        iItemRepository = mock(IItemRepository.class);
        itemRepositoryCustom = mock(ItemRepositoryCustom.class);
        iCommentRepository = mock(ICommentRepository.class);
        iUserService = mock(IUserService.class);
        iUserRepository = mock(IUserRepository.class);
        iRequestRepository = mock(IRequestRepository.class);
        itemService = new ItemService(iItemRepository, itemRepositoryCustom, iCommentRepository, iUserService, iUserRepository, iRequestRepository);

    }

    @Test
    void readAll() {

        User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Item item1 = new Item(1, "Item Name", "Item Desc", true, user1, new HashSet<>(), new HashSet<>(), null);

        when(itemRepositoryCustom.readAllItemsPaged(anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(item1));

        final List<ItemInfoDto> list = itemService.readAll(0, 10);

        assertNotNull(list);
        assertEquals(1, list.get(0).getId());
        assertEquals(1, list.size());

    }

    @Test
    void readAllUserItems() {

        Item item = new Item(1, "Item Name", "Item Desc", true, null, new HashSet<>(), new HashSet<>(), null);
        User user1 = new User(1, "email1@mail.com", "Name1", Collections.singletonList(item), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Item item1 = new Item(1, "Item Name", "Item Desc", true, user1, new HashSet<>(), new HashSet<>(), null);

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user1));

        when(itemRepositoryCustom.readAllUserItemsByUserIdPaged(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(item1));

        final List<ItemInfoDto> list = itemService.readAllUserItems(1, 0, 10);

        assertNotNull(list);
        assertEquals(1, list.get(0).getId());
        assertEquals(1, list.size());

    }

    @Test
    void createItem() {
        ItemDto itemDto = new ItemDto(null, "Item Name", "Item Desc", true, null);
        User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Item item1 = new Item(1, "Item Name", "Item Desc", true, user1, new HashSet<>(), new HashSet<>(), null);

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user1));

        when(iItemRepository.save(any()))
                .thenReturn(item1);

        when(iUserRepository.save(any()))
                .thenReturn(user1);

        final ItemInfoDto newItem = itemService.create(itemDto, 1);

        assertNotNull(newItem);
        assertEquals(1, newItem.getId());

    }

    @Test
    void readById() {

        ItemDto itemDto = new ItemDto(null, "Item Name", "Item Desc", true, null);
        User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Item item1 = new Item(1, "Item Name", "Item Desc", true, user1, new HashSet<>(), new HashSet<>(), null);

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user1));

        when(iItemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item1));

        when(itemRepositoryCustom.getItemsLastBooking(anyInt()))
                .thenReturn(Optional.empty());

        when(itemRepositoryCustom.getItemsNextBooking(anyInt()))
                .thenReturn(Optional.empty());

        final ItemInfoDto newItem = itemService.read(1, 1);

        assertNotNull(newItem);
        assertEquals(1, newItem.getId());

    }

    @Test
    void update() {

        ItemDto itemDto = new ItemDto(1, "Item Name", "Item Desc", true, null);
        User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Item item1 = new Item(1, "Item Name", "Item Desc", true, user1, new HashSet<>(), new HashSet<>(), null);

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user1));

        when(iItemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item1));

        when(iItemRepository.save(any()))
                .thenReturn(item1);

        final ItemInfoDto list = itemService.update(itemDto, 1, 1);

        assertNotNull(list);
        assertEquals(1, list.getId());

    }

    @Test
    void searchItems() {

        ItemDto itemDto = new ItemDto(null, "Item Name", "Item Desc", true, null);
        User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Item item1 = new Item(1, "Item Name", "Item Desc", true, user1, new HashSet<>(), new HashSet<>(), null);

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user1));

        when(itemRepositoryCustom.searchItemByWord(anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(item1));

        when(itemRepositoryCustom.getItemsLastBooking(anyInt()))
                .thenReturn(Optional.empty());

        when(itemRepositoryCustom.getItemsNextBooking(anyInt()))
                .thenReturn(Optional.empty());

        final List<ItemInfoDto> newItem = itemService.searchItemByWord("Item", 0, 10);

        assertNotNull(newItem);
        assertEquals(1, newItem.get(0).getId());

    }

    @Test
    void createNewComment() {

        Item item = new Item(1, "Item Name", "Item Desc", true, null, new HashSet<>(), new HashSet<>(), null);
        CommentInfoDto commentInfoDto = new CommentInfoDto(null, "comment", "Name1", LocalDateTime.now());

        User user2 = new User(2, "email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Booking lastBooking = new Booking(1, LocalDateTime.of(2022, 10, 1, 10, 00, 00),
                LocalDateTime.of(2022, 10, 2, 10, 00, 00), Status.APPROVED, item, user2);

        User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), Collections.singleton(lastBooking), new HashSet<>(), new HashSet<>());
        Item item1 = new Item(1, "Item Name", "Item Desc", true, user1, new HashSet<>(), new HashSet<>(), null);
        Comment comment = new Comment(1, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
                item1, user2);

        when(iUserService.getUser(anyInt()))
                .thenReturn(Optional.of(user1));

        when(iItemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item1));

        when(iItemRepository.checkUserBookedItemBeforeComment(anyInt(), anyInt(), any()))
                .thenReturn(Collections.singletonList(lastBooking));

        when(itemRepositoryCustom.getItemsLastBooking(anyInt()))
                .thenReturn(Optional.empty());

        when(itemRepositoryCustom.getItemsNextBooking(anyInt()))
                .thenReturn(Optional.empty());

        when(iCommentRepository.save(any()))
                .thenReturn(comment);

        final CommentInfoDto newComment = itemService.createComment(commentInfoDto, 1, 1);

        assertNotNull(newComment);
        assertEquals(1, newComment.getId());

    }
}
