package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.enums.BookingStatus;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long item;
    private Long booker;
    private BookingStatus status;
}
