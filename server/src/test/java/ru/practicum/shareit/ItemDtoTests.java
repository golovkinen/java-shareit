package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoTests {
    @Autowired
    private JacksonTester<ItemInfoDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemInfoDto itemDto = new ItemInfoDto(1, "Item1", "Desc1", true, new ItemInfoDto.BookingInfoForItemDto(1, 2), new ItemInfoDto.BookingInfoForItemDto(3, 4), new ArrayList<>(), null);

        JsonContent<ItemInfoDto> result = json.write(itemDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto.getAvailable());
    }
}
