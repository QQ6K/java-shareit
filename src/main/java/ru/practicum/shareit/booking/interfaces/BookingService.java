package ru.practicum.shareit.booking.interfaces;

import ru.practicum.shareit.booking.dto.BookingDtoExport;
import ru.practicum.shareit.booking.dto.BookingDtoImport;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingState;

import java.util.Collection;

public interface BookingService {

    Booking createBooking(Long userId, BookingDtoImport bookingDto);

    BookingDtoExport readById(Long bookingId);

    Collection<Booking> readAllUser(Long userId);

    Collection<Booking> readAllOwner(Long userId, String state);

    Booking updateBooking(Long userId, Long bookingId, Boolean approved);

}
