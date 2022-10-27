package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTests {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void readAll() throws Exception {
        when(userService.readAll())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    void create() throws Exception {

        UserDto userDto = new UserDto(1, "Email1@mail.com", "Name1");

        when(userService.create(any()))
                .thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email1@mail.com\",\"name\":\"Name1\"}"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("Email1@mail.com")))
                .andExpect(jsonPath("$.name", is("Name1")));

    }

    @Test
    void readById() throws Exception {

        UserDto userDto = new UserDto(1, "Email1@mail.com", "Name1");

        when(userService.read(anyInt()))
                .thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("Email1@mail.com")))
                .andExpect(jsonPath("$.name", is("Name1")));

    }

    @Test
    void update() throws Exception {

        UserDto userDto = new UserDto(1, "Email1@mail.com", "Name1");

        when(userService.update(any(), anyInt()))
                .thenReturn(userDto);

        mockMvc.perform(patch("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"Email1@mail.com\",\"name\":\"Name1\"}"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("Email1@mail.com")))
                .andExpect(jsonPath("$.name", is("Name1")));

    }

    @Test
    void deleteById() throws Exception {

        when(userService.delete(anyInt()))
                .thenReturn(HttpStatus.OK);

        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isOk());

    }
}
