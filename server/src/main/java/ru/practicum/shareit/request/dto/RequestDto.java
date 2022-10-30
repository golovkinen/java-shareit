package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestDto {
    private Integer id;
    private String description;
    private LocalDateTime created;
    private Integer requesterId;
    private List<ItemsForRequestDto> items;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ItemsForRequestDto {
        private Integer id;
        private String name;
        private String description;
        private Boolean available;
        private Integer requestId;
    }

}
