package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {
    private long id;
    @NotEmpty(message = "Пустое поле имя")
    private String name;
    @NotEmpty(message = "Пустое поле email")
    @Email(message = "Проверьте email")
    private String email;
}
