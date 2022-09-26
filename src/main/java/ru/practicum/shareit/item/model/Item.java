package ru.practicum.shareit.item.model;

import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Item {
    Long id;
    String name;
    String description;
    Boolean available;
    Long owner;
    String request;
}
