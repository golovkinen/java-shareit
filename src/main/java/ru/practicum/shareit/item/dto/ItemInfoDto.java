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


   /* public static class CommentDto{
        Integer id;
        String text;
        String authorName;

        public CommentDto(Integer id, String text, String authorName) {
            this.id = id;
            this.text = text;
            this.authorName = authorName;
        }
    } */
}
