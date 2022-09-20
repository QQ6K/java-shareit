package ru.practicum.shareit.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    @Min(value = 1, message = "id меньше 1")
    long id;

    @NotBlank(message = "Имя не может быть пустым.")
    String name;

    @Email(message = "email такого типа не существует")
    String email;
}
