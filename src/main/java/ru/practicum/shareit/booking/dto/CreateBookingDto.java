package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

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
