package ru.practicum.shareit.booking.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookingDtoExport;
import ru.practicum.shareit.booking.dto.BookingDtoImport;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService {

    Booking createBooking(Long userId, BookingDtoImport bookingDto);

    BookingDtoExport readById(Long bookingId, Long userId);

    Collection<Booking> readAllUser(Long userId, String state, Integer from, Integer size);

    Collection<Booking> readAllOwner(Long userId, String state, Integer size, Integer from);

    Booking updateBooking(Long userId, Long bookingId, Boolean approved);

}
