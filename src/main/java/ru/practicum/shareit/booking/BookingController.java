package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    @PostMapping
    public Optional<Booking> saveBooking(@Valid @RequestHeader("X-Sharer-User-Id")
                                   Long userId, @Valid @RequestBody BookingDto bookingDto) {
        log.info("Запрос 'POST /bookings'");
        return bookingService.createBooking(userId, bookingDto);
    }


}
