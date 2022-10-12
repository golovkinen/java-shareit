package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
public class CreateBookingDto {

    @NonNull
    private Integer itemId;
    @NonNull
    @FutureOrPresent
    private LocalDateTime start;
    @NonNull
    @FutureOrPresent
    private LocalDateTime end;
}
