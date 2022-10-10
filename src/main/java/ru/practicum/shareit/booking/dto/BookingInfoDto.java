package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingInfoDto {

    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private UserInfoDto booker;
    private ItemInfoForBookingDto item;

 /*   public static class UserInfoDto {
        Integer id;
        public UserInfoDto(Integer id) {
            this.id = id;
        }
    }

    public static class ItemInfoDto {
        Integer id;
        String name;

        public ItemInfoDto(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }*/
}
