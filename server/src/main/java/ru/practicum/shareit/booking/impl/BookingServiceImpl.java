package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import ru.practicum.shareit.exceptions.StateException;
import ru.practicum.shareit.exceptions.WrongUserException;
import ru.practicum.shareit.item.ItemsRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

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
        if (userId.equals(item.getOwner().getId())) {
            throw new CrudException("Пользователь не может бронировать свои вещи", "Пользователь",
                    userId.toString());
        }
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
    public BookingDtoExport readById(Long bookingId, Long userId) {
        Booking booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new CrudException("Бронирование не найдено", "id",
                        String.valueOf(bookingId)));
        User booker = usersRepository.findById(booking.getBooker().getId())
                .orElseThrow(() -> new CrudException("Пользователь не найден", "id",
                        String.valueOf(booking.getBooker())));
        Item item = itemsRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new CrudException("Вещь не найдена", "id",
                        String.valueOf(bookingId)));
        if (userId.equals(booking.getBooker().getId()) || userId.equals(booking.getItem().getOwner().getId())) {
            return BookingMapper.toDto(booking, booker, item);
        } else {
            throw new WrongUserException("Это бронирование недоступно для пользователя id=" + userId);
        }
    }

    @Override
    public Collection<Booking> readAllUser(Long userId, String state, Integer from, Integer size) {
        usersRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        if (state == null) {
            state = "ALL";
        }
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
            log.info("Просмотр бронирования пользователя id: {}", userId);
        } catch (IllegalArgumentException e) {
            throw new StateException("Unknown state: UNSUPPORTED_STATUS");
        }
      Pageable pageable;
        if (size == null || from == null) {
            pageable = Pageable.unpaged();
        } else if (size <= 0 || from < 0) {
            throw new BadRequestException("Ошибка параметров пагинации");
        } else {
            int page = from / size;
            pageable = PageRequest.of(page, size);
        }
        switch (bookingState) {
            case ALL:
                return bookingsRepository.findBookingByBooker_IdOrderByStartDateDesc(userId, pageable).getContent();
            case FUTURE:
                return bookingsRepository.findFuture(userId, LocalDateTime.now(), pageable).getContent();
            case PAST:
                return bookingsRepository.findByBookerIdStatePast(userId,
                        LocalDateTime.now(), BookingStatus.APPROVED, pageable).getContent();
            case CURRENT:
                return bookingsRepository.findByBookerIdStateCurrent(userId, LocalDateTime.now(),pageable).getContent();
            case REJECTED:
                return bookingsRepository.findByBooker_IdAndStatus(userId, BookingStatus.REJECTED, pageable).getContent();
            case WAITING:
                return bookingsRepository.findByBooker_IdAndStatus(userId, BookingStatus.WAITING, pageable).getContent();
            default:
                throw new BadRequestException("Неизвестный статус");
        }
    }

    @Override
    public Collection<Booking> readAllOwner(Long userId, String state, Integer size, Integer from) {
        usersRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        BookingState bookingState;
        if (state == null) {
            state = "ALL";
        }
        Pageable pageable;
        if (size == null || from == null) {
            pageable = Pageable.unpaged();
        } else if (size < 0 || from < 0) {
            throw new BadRequestException("Ошибка параметров пагинации");
        } else {
            int page = from / size;
            pageable = PageRequest.of(page, size);
        }
        try {
            bookingState = BookingState.valueOf(state);
            log.info("Просмотр бронирования владельца id: {}", userId);
        } catch (IllegalArgumentException e) {
            throw new StateException("Unknown state: UNSUPPORTED_STATUS");
        }

        switch (bookingState) {
            case ALL:
                return bookingsRepository.findOwnerAll(userId,pageable).getContent();
            case FUTURE:
                return bookingsRepository.findOwnerFuture(userId, LocalDateTime.now(),pageable).getContent();
            case PAST:
                return bookingsRepository.findOwnerPast(userId,
                        LocalDateTime.now(), pageable).getContent();
            case CURRENT:
                return bookingsRepository.findOwnerCurrent(userId,
                        LocalDateTime.now(),pageable).getContent();
            case REJECTED:
                return bookingsRepository.findByOwnerIdAndStatus(userId, BookingStatus.REJECTED, pageable).getContent();
            case WAITING:
                return bookingsRepository.findByOwnerIdAndStatus(userId, BookingStatus.WAITING,pageable).getContent();
            default:
                throw new StateException("Неверный статус");
        }
    }

    @Override
    @Transactional
    public Booking updateBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingsRepository.findById(bookingId).orElseThrow(() ->
                new CrudException("Брони не существует",
                        "id", String.valueOf(bookingId)));
        usersRepository.findById(booking.getBooker().getId()).orElseThrow(() ->
                new CrudException("Пользователя не существует",
                        "id", String.valueOf(booking.getBooker().getId())));
        usersRepository.findById(userId).orElseThrow(() ->
                new CrudException("Пользователя не существует",
                        "id", String.valueOf(userId)));
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BadRequestException("Несоответствие статуса бронирования " + userId);
        }
        if (approved.equals(true)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else if (approved.equals(false)) {
            booking.setStatus(BookingStatus.REJECTED);
        }
        Item item = itemsRepository.findById(booking.getItem().getId()).orElseThrow(() ->
                new CrudException("Вещи не существует",
                        "id", String.valueOf(bookingId)));
        if (!item.getOwner().getId().equals(userId)) {
            throw new WrongUserException("Это бронирование недоступно для пользователя id=" + userId);
        }
        log.info("Изменение статуса бронирования  id: {}", booking.getId());
        return bookingsRepository.save(booking);
    }


}
