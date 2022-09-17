package ru.practicum.shareit.request;

import lombok.*;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemRequest {
    Long id;
    String description;
    Long requestor;
    LocalDateTime created;
}
