package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemOutDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Long requestId;
}
