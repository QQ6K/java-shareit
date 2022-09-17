package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.enums.BookingStatus;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Booking {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long item;
    Long booker;
    BookingStatus status;
}
