package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemDto {

    private Integer id;
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    @NotBlank
    @Size(max = 200)
    private String description;
    @NotNull
    private Boolean available;

}
