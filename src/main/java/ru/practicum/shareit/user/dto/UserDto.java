package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private Integer id;

    @NonNull
    @NotBlank
    @Email(message = "Please enter a valid e-mail address")
    private String email;

    @NonNull
    @NotBlank
    private String name;
}
