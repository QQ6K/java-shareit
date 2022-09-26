package ru.practicum.shareit.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    private Long id;
    private String name;
    private String email;
}
