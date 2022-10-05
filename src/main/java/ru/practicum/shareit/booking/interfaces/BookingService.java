package ru.practicum.shareit.booking.interfaces;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.Optional;

public interface BookingService {

    Optional<Booking> createBooking(Long userId, BookingDto bookingDto);

    Booking readById(Long bookingId);

    Collection<Booking> readAll(Long userId);

    Optional<Booking> updateBooking();

    void deleteItem();

}
