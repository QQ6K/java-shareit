package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingsRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingsRepository bookingsRepository;
    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Override
    public Optional<Booking> createBooking(Long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
       /* User owner = userRepository.findById(bookingDto.getId()).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(bookingDto.getId())));*/
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new CrudException("Вещи не существует",
                "id", String.valueOf(bookingDto.getItemId())));
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь занята id=" + item.getId());}
        if (bookingDto.getStart().isBefore(LocalDateTime.now()) ||bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Дата старта бронирования или окончания в прошлом");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new BadRequestException("Окончание бронирования раньше старта окончания бронирования");
        }
        Booking booking = BookingMapper.fromDto(bookingDto);
        booking.setBooker_id(userId);
        bookingsRepository.save(booking);
        log.info("Создание бронирования  id: {}", booking.getId());
        return bookingsRepository.findById(booking.getId());
    }

    @Override
    public Booking readById(Long bookingId) {
        return bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new CrudException("Бронирование не найдено", "id", String.valueOf(bookingId)));
    }

    @Override
    public Collection<Booking> readAll(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        return bookingsRepository.findAll().stream()
                .filter(item -> item.getBooker_id().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Booking> updateBooking() {
        return Optional.empty();
    }

    @Override
    public void deleteItem() {

    }
}
