package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
											  @RequestParam(name = "state", defaultValue = "all") String stateParam,
											  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
											  Integer from,
											  @RequestParam(required = false) Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Запрос 'GET /bookings/owner' пользователя " + userId);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsOwner(@RequestParam(name = "state", defaultValue = "all")
												   String stateParam,
												   @RequestHeader("X-Sharer-User-Id") Long userId,
												   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
												   @RequestParam(required = false) Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Запрос 'GET /bookings' пользователя " + userId);
		return bookingClient.getBookingsOwner(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
										 @RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Запрос 'POST /bookings'");
		return bookingClient.create(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
											 @PathVariable Long bookingId) {
		log.info("Запрос 'GET /bookings/{}'", bookingId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@ResponseBody
	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> update(@PathVariable Long bookingId,
										 @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam Boolean approved) {
		log.info("Запрос 'PATCH /bookings/{}'", bookingId);
		return bookingClient.update(bookingId, userId, approved);
	}
}
