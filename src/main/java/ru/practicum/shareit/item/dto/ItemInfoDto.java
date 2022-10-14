package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemInfoDto {

    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private BookingInfoForItemDto lastBooking;
    private BookingInfoForItemDto nextBooking;
    private List<CommentInfoDto> comments;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class BookingInfoForItemDto {
        private Integer id;
        private Integer bookerId;
    }
}
