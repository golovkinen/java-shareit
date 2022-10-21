package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
public class CreateBookingDto {

    @NonNull
    private Integer itemId;
    @NonNull
    @FutureOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime start;
    @NonNull
    @FutureOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime end;
}
