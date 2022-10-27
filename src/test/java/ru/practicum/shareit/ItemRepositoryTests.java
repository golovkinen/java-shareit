package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ICommentRepository;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.IUserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = "/test_schema.sql")
public class ItemRepositoryTests {


    @Autowired
    IItemRepository iItemRepository;
    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    ICommentRepository iCommentRepository;


    User user1;
    User user2;
    Item item1;
    Item item2;
    Comment comment1;

    @BeforeEach
    void beforeEach() {

        user1 = iUserRepository.save(new User(null, "Email1@mail.com", "Name1", Collections.singletonList(item1), new HashSet<>(), new HashSet<>(), new HashSet<>()));
        user2 = iUserRepository.save(new User(null, "Email2@mail.com", "Name2", Collections.singletonList(item2), new HashSet<>(), new HashSet<>(), Collections.singleton(comment1)));
        item1 = iItemRepository.save(new Item(null, "Item Name1", "Item1 Desc", true, user1, new HashSet<>(), Collections.singleton(comment1), null));
        comment1 = iCommentRepository.save(new Comment(null, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
                item1, user2));
        item2 = iItemRepository.save(new Item(null, "Item Name2", "Item2 Desc", true, user2, new HashSet<>(), new HashSet<>(), null));
    }

    @Test
    void readAll() {
        List<Item> list = iItemRepository.findAllPaged(PageRequest.of(0, 10));

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0).getId());
        assertEquals(2, list.get(1).getId());
    }

    @Test
    void readAllUserItems() {
        List<Item> list = iItemRepository.readAllUserItemsByUserIdPaged(1, PageRequest.of(0, 10));

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getId());
    }

    @Test
    void readById() {
        Optional<Item> list = iItemRepository.findById(2);

        assertNotNull(list);
        assertEquals(2, list.get().getId());
    }

    @Test
    void createNew() {
        Item list = iItemRepository.save(new Item(null, "Item Name3", "Item3 Desc", true, user2, new HashSet<>(), new HashSet<>(), null));

        assertNotNull(list);
        assertEquals(3, list.getId());
    }

    @Test
    void deleteByUser() {
        iItemRepository.deleteById(1);
        Optional<Item> list = iItemRepository.findById(1);
        assertTrue(list.isEmpty());
    }

    @Test
    void createNewComment() {
        Comment list = iCommentRepository.save(new Comment(null, "Comment", LocalDateTime.of(2022, 10, 2, 10, 00, 00),
                item1, user2));
        assertNotNull(list);
        assertEquals(2, list.getId());
    }
}