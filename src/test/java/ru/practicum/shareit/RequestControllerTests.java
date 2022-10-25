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
import ru.practicum.shareit.request.RequestController;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
@AutoConfigureMockMvc
public class RequestControllerTests {

    @MockBean
    RequestService requestService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllUserRequests() throws Exception {
        when(requestService.readAllUserRequests(anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    void getAllRequests() throws Exception {

        RequestDto request = new RequestDto(1, "Request Desc", LocalDateTime.now().plusMinutes(10).withNano(0), null, new ArrayList<>());

        when(requestService.readAll(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(request));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is(request.getDescription())))
                .andExpect(jsonPath("$[0].created", is(String.valueOf(request.getCreated()))));

    }

    @Test
    void getRequestById() throws Exception {

        RequestDto request = new RequestDto(1, "Request Desc", LocalDateTime.now().plusMinutes(10).withNano(0), null, new ArrayList<>());

        when(requestService.read(anyInt(), anyInt()))
                .thenReturn(request);

        mockMvc.perform(get("/requests/{id}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.created", is(String.valueOf(request.getCreated()))));

    }

    @Test
    void createRequest() throws Exception {

        LocalDateTime created = LocalDateTime.now().withNano(0);

        RequestDto request = new RequestDto(null, "Request Desc", null, 1, null);

        RequestDto requestCreated = new RequestDto(1, "Request Desc", created, 1, new ArrayList<>());

        when(requestService.create(any(), anyInt()))
                .thenReturn(requestCreated);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is(request.getDescription())));

    }


    @Test
    void updateRequest() throws Exception {

        RequestDto request = new RequestDto(1, "Request Desc", LocalDateTime.now().plusMinutes(10).withNano(0), 1, new ArrayList<>());

        when(requestService.update(any(), anyInt(), anyInt()))
                .thenReturn(request);

        mockMvc.perform(patch("/requests/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.created", is(String.valueOf(request.getCreated()))));

    }

    @Test
    void deleteByIdRequest() throws Exception {

        RequestDto request = new RequestDto(1, "Request Desc", LocalDateTime.now().plusMinutes(10).withNano(0), 1, new ArrayList<>());

        when(requestService.delete(anyInt(), anyInt()))
                .thenReturn(HttpStatus.OK);

        mockMvc.perform(delete("/requests/{id}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

    }
}
