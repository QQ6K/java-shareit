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
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.item.ItemsRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
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
        Booking booking = new Booking(0L, bookingDto.getStart(), bookingDto.getEnd(), bookingDto.getItemId(), userId, BookingStatus.WAITING);
        booking.setBooker_id(userId);
        booking = bookingsRepository.save(booking);
        log.info("Создание бронирования  id: {}", booking.getId());
        return booking;
    }

    @Override
    public Booking readById(Long bookingId) {
        return bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new CrudException("Бронирование не найдено", "id", String.valueOf(bookingId)));
    }

    @Override
    public Collection<Booking> readAllUser(Long userId) {
        usersRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        return bookingsRepository.findAll().stream()
                .filter(item -> item.getBooker_id().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Booking> readAllOwner(Long userId) {
        usersRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        return bookingsRepository.findAll().stream()
                .filter(item -> item.getBooker_id().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public BookingDtoExport updateBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingsRepository.findById(bookingId).orElseThrow(() ->
                new CrudException("Вещи не существует",
                        "id", String.valueOf(bookingId)));
        if (approved.equals(true)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else if (approved.equals(false)) {
        }
        Item item = itemsRepository.findById(booking.getItem_id()).orElseThrow(() ->
                new CrudException("Вещи не существует",
                        "id", String.valueOf(bookingId)));
        User user = usersRepository.findById(userId).orElseThrow(() ->
                new CrudException("Пользователя не существует",
                        "id", String.valueOf(bookingId)));
        log.info("Изменение статуса бронирования  id: {}", booking.getId());
        return BookingMapper.toDto(booking, user, item);
    }
}
