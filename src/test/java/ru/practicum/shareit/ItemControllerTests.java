package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTests {

    @MockBean
    ItemService itemService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void readAllItems() throws Exception {
        when(itemService.readAll(anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/all"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    void create() throws Exception {

        ItemDto itemDto = new ItemDto(null, "Отвертка", "Отвертка крестовая", false, null);

        ItemInfoDto itemInfoDto = new ItemInfoDto(1, "Отвертка", "Отвертка крестовая", false, null, null, new ArrayList<>(), null);

        when(itemService.create(any(), anyInt()))
                .thenReturn(itemInfoDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Отвертка")))
                .andExpect(jsonPath("$.description", is("Отвертка крестовая")))
                .andExpect(jsonPath("$.available", is(false)));

    }

    @Test
    void readById() throws Exception {

        ItemDto itemDto = new ItemDto(null, "Отвертка", "Отвертка крестовая", false, null);

        ItemInfoDto itemInfoDto = new ItemInfoDto(1, "Отвертка", "Отвертка крестовая", false, null, null, new ArrayList<>(), null);

        when(itemService.read(anyInt(), anyInt()))
                .thenReturn(itemInfoDto);

        mockMvc.perform(get("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Отвертка")))
                .andExpect(jsonPath("$.description", is("Отвертка крестовая")))
                .andExpect(jsonPath("$.available", is(false)));

    }

    @Test
    void update() throws Exception {

        ItemDto itemDto = new ItemDto(null, "Отвертка", "Отвертка крестовая", false, null);

        ItemInfoDto itemInfoDto = new ItemInfoDto(1, "Отвертка", "Отвертка крестовая", false, null, null, new ArrayList<>(), null);

        when(itemService.update(any(), anyInt(), anyInt()))
                .thenReturn(itemInfoDto);

        mockMvc.perform(patch("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Отвертка")))
                .andExpect(jsonPath("$.description", is("Отвертка крестовая")))
                .andExpect(jsonPath("$.available", is(false)));

    }

    @Test
    void deleteById() throws Exception {

        when(itemService.delete(anyInt(), anyInt()))
                .thenReturn(HttpStatus.OK);

        mockMvc.perform(delete("/items/{id}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

    }

    @Test
    void deleteAllUserItems() throws Exception {

        when(itemService.deleteAllUserItems(anyInt()))
                .thenReturn(HttpStatus.OK);

        mockMvc.perform(delete("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

    }

    @Test
    void readAllUserItems() throws Exception {
        ItemDto itemDto = new ItemDto(null, "Отвертка", "Отвертка крестовая", false, null);

        ItemInfoDto itemInfoDto = new ItemInfoDto(1, "Отвертка", "Отвертка крестовая", false, null, null, new ArrayList<>(), null);

        when(itemService.readAllUserItems(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(itemInfoDto));

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Отвертка")))
                .andExpect(jsonPath("$[0].description", is("Отвертка крестовая")))
                .andExpect(jsonPath("$[0].available", is(false)));
    }

    @Test
    void searchItems() throws Exception {
        ItemDto itemDto = new ItemDto(null, "Отвертка", "Отвертка крестовая", false, null);

        ItemInfoDto itemInfoDto = new ItemInfoDto(1, "Отвертка", "Отвертка крестовая", true, null, null, new ArrayList<>(), null);

        when(itemService.searchItemByWord(anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(itemInfoDto));

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "Отвертка"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Отвертка")))
                .andExpect(jsonPath("$[0].description", is("Отвертка крестовая")))
                .andExpect(jsonPath("$[0].available", is(true)));
    }

    @Test
    void createComment() throws Exception {

        CommentInfoDto commentDto = new CommentInfoDto(null, "Comment for Item", null, null);

        CommentInfoDto comment = new CommentInfoDto(1, "Comment for Item", "Name1", LocalDateTime.of(2022, 10, 1, 10, 00, 00));

        when(itemService.createComment(any(), anyInt(), anyInt()))
                .thenReturn(comment);

        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(commentDto)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Comment for Item")))
                .andExpect(jsonPath("$.authorName", is("Name1")))
                .andExpect(jsonPath("$.created", is("2022-10-01T10:00:00")));

    }
}
