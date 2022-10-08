package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingsRepository;
import ru.practicum.shareit.booking.dto.BookingDtoExport;
import ru.practicum.shareit.booking.dto.BookingDtoImport;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingState;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.item.ItemsRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingsRepository bookingsRepository;
    private final UsersRepository usersRepository;
    private final ItemsRepository itemsRepository;

    @Override
    @Transactional
    public Booking createBooking(Long userId, BookingDtoImport bookingDto) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new CrudException("Пользователя не существует",
                        "id", String.valueOf(userId)));
        Item item = itemsRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new CrudException("Вещи не существует",
                        "id", String.valueOf(bookingDto.getItemId())));
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь занята id=" + item.getId());
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now()) || bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Дата старта бронирования или окончания в прошлом");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new BadRequestException("Окончание бронирования раньше старта окончания бронирования");
        }
        Booking booking = new Booking(0L, bookingDto.getStart(), bookingDto.getEnd(), item, user, BookingStatus.WAITING);
        booking = bookingsRepository.save(booking);
        log.info("Создание бронирования  id: {}", booking.getId());
        return booking;
    }

    @Override
    public BookingDtoExport readById(Long bookingId) {
        Booking booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new CrudException("Бронирование не найдено", "id",
                        String.valueOf(bookingId)));
        User booker = usersRepository.findById(booking.getBooker().getId())
                .orElseThrow(() -> new CrudException("Пользователь не найден", "id",
                        String.valueOf(booking.getBooker())));
        Item item = itemsRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new CrudException("Бронирование не найдено", "id",
                        String.valueOf(bookingId)));
        return BookingMapper.toDto(booking, booker, item);
    }

    @Override
    public Collection<Booking> readAllUser(Long userId) {
        usersRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        Collection<Booking> bookings = bookingsRepository.findAll().stream()
                .filter(user -> user.getBooker().getId().equals(userId))
                .sorted(Comparator.comparing(Booking::getStart_date).reversed())
                .collect(Collectors.toList());
        Collection<Booking> bookings1 = bookingsRepository.findAll().stream()
                .collect(Collectors.toList());
        return bookings;
    }

    @Override
    public Collection<Booking> readAllOwner(Long userId, String state) {
        BookingState bookingState = null;
        try {
            bookingState = BookingState.valueOf(state);
        } finally {
            bookingState = BookingState.ALL;
        }
        switch (bookingState) {
            case ALL:
                return readAllUser(userId);
            case FUTURE:
            case PAST:
            case CURRENT:
            case REJECTED:
            default:
                return readAllUser(userId);
        }
        //log.info("Запрос бронирование владельца id: {}", userId);
       // return null; // bookings;
    }

    @Override
    @Transactional
    public Booking updateBooking(Long userId, Long bookingId, Boolean approved) {
        Collection<Booking> bookings = bookingsRepository.findAll().stream()
                .collect(Collectors.toList());
        Booking booking = bookingsRepository.findById(bookingId).orElseThrow(() ->
                new CrudException("Вещи не существует",
                        "id", String.valueOf(bookingId)));
        if (approved.equals(true)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else if (approved.equals(false)) {
            booking.setStatus(BookingStatus.REJECTED);
        }
        Item item = itemsRepository.findById(booking.getItem().getId()).orElseThrow(() ->
                new CrudException("Вещи не существует",
                        "id", String.valueOf(bookingId)));
        User user = usersRepository.findById(booking.getBooker().getId()).orElseThrow(() ->
                new CrudException("Пользователя не существует",
                        "id", String.valueOf(bookingId)));
        log.info("Изменение статуса бронирования  id: {}", booking.getId());
        bookings = bookingsRepository.findAll().stream()
                .collect(Collectors.toList());
        return bookingsRepository.save(booking);
    }

}
