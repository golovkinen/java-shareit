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
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTests {

    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void readAllUserBookings() throws Exception {
        when(bookingService.readAllUserBookings(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    void create() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusMinutes(5).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(10).withNano(0);

        CreateBookingDto createBookingDto = new CreateBookingDto(1, start, end);

        BookingInfoDto bookingInfoDto = new BookingInfoDto(1, start, end, Status.WAITING, null, new BookingInfoDto.ItemInfoForBookingDto(1, "Item"));

        when(bookingService.create(anyInt(), any()))
                .thenReturn(bookingInfoDto);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(createBookingDto)))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.start", is(String.valueOf(start))))
                .andExpect(jsonPath("$.end", is(String.valueOf(end))))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.WAITING))));

    }

    @Test
    void readById() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusMinutes(5).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(10).withNano(0);

        CreateBookingDto createBookingDto = new CreateBookingDto(1, start, end);

        BookingInfoDto bookingInfoDto = new BookingInfoDto(1, start, end, Status.WAITING, null, new BookingInfoDto.ItemInfoForBookingDto(1, "Item"));

        when(bookingService.read(anyInt(), anyInt()))
                .thenReturn(bookingInfoDto);

        mockMvc.perform(get("/bookings/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.start", is(String.valueOf(start))))
                .andExpect(jsonPath("$.end", is(String.valueOf(end))))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.WAITING))));

    }

    @Test
    void getAllUserBookings() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusMinutes(5).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(10).withNano(0);

        CreateBookingDto createBookingDto = new CreateBookingDto(1, start, end);

        BookingInfoDto bookingInfoDto = new BookingInfoDto(1, start, end, Status.WAITING, null, new BookingInfoDto.ItemInfoForBookingDto(1, "Item"));

        when(bookingService.readBookingListOfAllUserItems(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(bookingInfoDto));

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].start", is(String.valueOf(start))))
                .andExpect(jsonPath("$[0].end", is(String.valueOf(end))))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(Status.WAITING))));

    }

    @Test
    void update() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusMinutes(5).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMinutes(10).withNano(0);

        CreateBookingDto createBookingDto = new CreateBookingDto(1, start, end);

        BookingInfoDto bookingInfoDto = new BookingInfoDto(1, start, end, Status.APPROVED, null, new BookingInfoDto.ItemInfoForBookingDto(1, "Item"));

        when(bookingService.bookingApproval(anyString(), anyInt(), anyInt()))
                .thenReturn(bookingInfoDto);

        mockMvc.perform(patch("/bookings/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.start", is(String.valueOf(start))))
                .andExpect(jsonPath("$.end", is(String.valueOf(end))))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.APPROVED))));

    }

    @Test
    void deleteById() throws Exception {

        when(bookingService.delete(anyInt(), anyInt()))
                .thenReturn(HttpStatus.OK);

        mockMvc.perform(delete("/bookings/{id}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

    }
}
