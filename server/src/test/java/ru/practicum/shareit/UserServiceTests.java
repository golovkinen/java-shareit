package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.IUserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTests {


    IUserRepository iUserRepository;

    UserService userService;

    @BeforeEach
    void beforeEach() {
        iUserRepository = mock(IUserRepository.class);
        userService = new UserService(iUserRepository);

    }

    @Test
    void readAll() {

        User user1 = new User(1, "email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());

        when(iUserRepository.findAll())
                .thenReturn(Collections.singletonList(user1));

        final List<UserDto> list = userService.readAll();

        assertNotNull(list);
        assertEquals(1, list.get(0).getId());
        assertEquals(1, list.size());

    }

    @Test
    void createUser() {
        UserDto userDto = new UserDto(null, "Email1@mail.com", "Name1");
        User user1 = new User(1, "Email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());


        when(iUserRepository.save(any()))
                .thenReturn(user1);

        final UserDto newUser = userService.create(userDto);

        assertNotNull(newUser);
        assertEquals(1, newUser.getId());

    }

    @Test
    void readById() {

        UserDto userDto = new UserDto(1, "Email1@mail.com", "Name1");
        User user1 = new User(1, "Email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());


        when(iUserRepository.findById(anyInt()))
                .thenReturn(Optional.of(user1));

        final UserDto findUser = userService.read(1);

        assertNotNull(findUser);
        assertEquals(1, findUser.getId());

    }

    @Test
    void update() {

        UserDto userDto = new UserDto(1, "UpdatedEmail1@mail.com", "UpdatedName1");
        User user1 = new User(1, "Email1@mail.com", "Name1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        User user2 = new User(1, "UpdatedEmail1@mail.com", "UpdatedName1", new ArrayList<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());


        when(iUserRepository.findById(anyInt()))
                .thenReturn(Optional.of(user1));

        when(iUserRepository.save(any()))
                .thenReturn(user1);

        final UserDto updateUser = userService.update(userDto, 1);

        assertNotNull(updateUser);
        assertEquals(1, updateUser.getId());

    }
}
