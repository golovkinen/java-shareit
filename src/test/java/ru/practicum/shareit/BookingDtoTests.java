package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.enums.Status;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoTests {
    @Autowired
    private JacksonTester<BookingInfoDto> json;

    @Test
    void testSerialize() throws Exception {

        LocalDateTime start = LocalDateTime.of(2022, 10, 1, 10, 00, 00);
        LocalDateTime end = LocalDateTime.of(2022, 10, 2, 10, 00, 00);

        BookingInfoDto bookingInfoDto = new BookingInfoDto(1, start, end, Status.APPROVED,
                new BookingInfoDto.UserInfoDto(1), new BookingInfoDto.ItemInfoForBookingDto(1, "Item1"));

        JsonContent<BookingInfoDto> result = json.write(bookingInfoDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingInfoDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-10-01T10:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-10-02T10:00:00");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(String.valueOf(Status.APPROVED));

    }
}
