package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {
    private long id;
    private String name;
    @Email(message = "email should be a valid email")
    private String email;
}
