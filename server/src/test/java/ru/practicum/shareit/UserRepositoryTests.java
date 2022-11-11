package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.IUserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = "/test_schema.sql")
public class UserRepositoryTests {

    @Autowired
    IUserRepository iUserRepository;

    User user1;

    User user2;

    @BeforeEach
    void beforeEach() {
        user1 = iUserRepository.save(new User(1, "Email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()));
        user2 = iUserRepository.save(new User(2, "Email2@mail.com", "Name2", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()));

    }

    @Test
    void readAll() {
        List<User> list = iUserRepository.findAll();

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0).getId());
        assertEquals(2, list.get(1).getId());
    }

    @Test
    void readById() {
        Optional<User> list = iUserRepository.findById(2);

        assertNotNull(list);
        assertEquals(2, list.get().getId());
    }

    @Test
    void createNew() {
        User list = iUserRepository.save(new User(null, "Email3@mail.com", "Name3", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()));


        assertNotNull(list);
        assertEquals(3, list.getId());
    }

    @Test
    void deleteByUser() {
        iUserRepository.delete(user1);
        Optional<User> list = iUserRepository.findById(1);
        assertTrue(list.isEmpty());
    }
}