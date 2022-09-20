package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
//@RequiredArgsConstructor
//@EqualsAndHashCode(of = "id")
    public class UserDto {

        @EqualsAndHashCode.Include
        private final long id;

        private final String name;

        private final String email;
    }
