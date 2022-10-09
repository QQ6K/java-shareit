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
import ru.practicum.shareit.exceptions.StateException;
import ru.practicum.shareit.exceptions.WrongUserException;
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
    public Collection<Booking> readAllUser(Long userId, String state) {
        usersRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);}
        catch (IllegalArgumentException e){
            throw new StateException("Unknown state: UNSUPPORTED_STATUS");
        }
            switch (bookingState) {
                case ALL:
                    return  bookingsRepository.findBookingByBooker_IdOrderByStartDateDesc(userId);
                case FUTURE:
                    return  bookingsRepository.findFuture(userId, LocalDateTime.now());
                case PAST:
                    return bookingsRepository.findByBookerIdStatePast(userId,
                            LocalDateTime.now());
                case CURRENT:
                    return bookingsRepository.findByBookerIdStateCurrent(userId, LocalDateTime.now());
                case REJECTED:
                    return bookingsRepository.findByBooker_IdAndStatus(userId, BookingStatus.REJECTED);
                case WAITING:
                    return bookingsRepository.findByBooker_IdAndStatus(userId, BookingStatus.WAITING);
                default: throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
            }
    }

    @Override
    public Collection<Booking> readAllOwner(Long userId, String state) {
        usersRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        }
            catch (IllegalArgumentException e) {
                throw new StateException("Unknown state: UNSUPPORTED_STATUS");
            }
            switch (bookingState) {
                case ALL:
                    return bookingsRepository.findOwnerAll(userId);
                case FUTURE:
                    return bookingsRepository.findOwnerFuture(userId, LocalDateTime.now());
                case PAST:
                    return bookingsRepository.findOwnerPast(userId,
                            LocalDateTime.now());
                case CURRENT:
                    return bookingsRepository.findOwnerCurrent(userId,
                            LocalDateTime.now());
                case REJECTED:
                    return bookingsRepository.findByOwnerIdAndStatus(userId, BookingStatus.REJECTED);
                case WAITING:
                    return bookingsRepository.findByOwnerIdAndStatus(userId, BookingStatus.WAITING);
                default:throw new StateException("Unknown state: UNSUPPORTED_STATUS");
        }
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
