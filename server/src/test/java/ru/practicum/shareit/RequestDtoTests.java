package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestDtoTests {
    @Autowired
    private JacksonTester<RequestDto> json;

    @Test
    void testSerialize() throws Exception {

        LocalDateTime start = LocalDateTime.of(2022, 10, 1, 10, 00, 00);

        RequestDto requestDto = new RequestDto(1, "Desc", start, 2,
                Collections.singletonList(new RequestDto.ItemsForRequestDto(1, "Item", "Desc Item", true, 1)));

        JsonContent<RequestDto> result = json.write(requestDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(requestDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2022-10-01T10:00:00");

    }
}
