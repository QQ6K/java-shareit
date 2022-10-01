package ru.practicum.shareit.request;

import lombok.*;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemRequest {
    private Long id;
    private String description;
    private Long requestor;
    private LocalDateTime created;
}
