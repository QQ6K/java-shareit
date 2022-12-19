package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoExport;
import ru.practicum.shareit.booking.dto.BookingDtoImport;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking saveBooking(@Valid @RequestHeader("X-Sharer-User-Id")
                               Long userId, @Valid @RequestBody BookingDtoImport bookingDto) {
        log.info("Запрос 'POST /bookings'");
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking patchBooking(@RequestHeader("X-Sharer-User-Id")
                                @Valid @NotNull(message = "Отсутсвует X-Sharer-User-Id") long userId,
                                @PathVariable Long bookingId, @RequestParam Boolean approved) {
        log.info("Запрос 'PATCH /bookings/{}'", bookingId);
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoExport readById(@RequestHeader("X-Sharer-User-Id")
                                     long userId,
                                     @PathVariable long bookingId) {
        log.info("Запрос 'GET /bookings/{}'", bookingId);
        return bookingService.readById(bookingId, userId);
    }

    @GetMapping
    public Collection<Booking> readAllUser(@RequestHeader("X-Sharer-User-Id")
                                           @NotNull(message = "Отсутсвует X-Sharer-User-Id") Long userId,
                                           @RequestParam(name = "state", required = false) String state,
                                           @RequestParam(name = "from", required = false) Integer from,
                                           @RequestParam(name = "size", required = false) Integer size) {
        log.info("Запрос 'GET /bookings' пользователя " + userId);
        return bookingService.readAllUser(userId, state,from,size);
    }

    @GetMapping("/owner")
    public Collection<Booking> readAllOwner(@RequestHeader("X-Sharer-User-Id")
                                            @NotNull(message = "Отсутсвует X-Sharer-User-Id") Long userId,
                                            @RequestParam(name = "state", required = false) String state,
                                            @RequestParam(name = "from", required = false) Integer from,
                                            @RequestParam(name = "size", required = false) Integer size
                                            ) {
        log.info("Запрос 'GET /bookings/owner' пользователя " + userId);
        return bookingService.readAllOwner(userId, state, from, size);
    }

}
