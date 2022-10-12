package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.enums.Status;

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

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserInfoDto {
        Integer id;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ItemInfoForBookingDto {
        Integer id;
        String name;

    }
}
