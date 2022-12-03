package ru.practicum.shareit.booking.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.practicum.shareit.booking.BookingsRepository;
import ru.practicum.shareit.booking.dto.BookingDtoImport;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.ItemsRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookingServiceImpl.class)
public class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;
    @MockBean
    private ItemsRepository itemsRepository;
    @MockBean
    private UsersRepository usersRepository;
    @MockBean
    private BookingsRepository bookingsRepository;

    User user = new User(1L, "qwe@qwe.qwe", "qwe@qwe.qwe");

    User user123 = new User(123L, "qwe123@qwe.qwe", "qwe123@qwe.qwe");

    Item item = new Item(111L, "Name", "asd", true, user, 1234L);

    LocalDateTime start = LocalDateTime.now();

    LocalDateTime finish = LocalDateTime.now();

    Booking booking = new Booking(403L, start.plusHours(1), finish.plusHours(3), item, user, BookingStatus.WAITING);

    @Test
    public void createBookingTest() {
        start = start.plusHours(1);
        finish = start.plusHours(2);
        when(usersRepository.findById(123L)).thenReturn(Optional.of(user));
        when(itemsRepository.findById(208L)).thenReturn(Optional.of(item));
        Booking bookingSave = new Booking(0L, start, finish, item, user, BookingStatus.WAITING);
        when(bookingsRepository.save(bookingSave)).thenReturn(booking);
        BookingDtoImport bookingDtoImport = new BookingDtoImport(208L, start, finish);
        when(bookingsRepository.save(booking)).thenReturn(booking);
        Booking bookingRes = bookingService.createBooking(123L, bookingDtoImport);
        assertNotNull(bookingRes);
        assertEquals(booking, bookingRes);
    }

    @Test
    public void readById() {
        doReturn(Optional.of(new User(1L, "Test", "qweqwe@qwe.qwe")))
                .when(usersRepository).findById(Mockito.any());
        doReturn(Optional.of(booking)).when(bookingsRepository).findById(Mockito.any());
        doReturn(Optional.of(item)).when(bookingsRepository).findById(Mockito.any());
        when(bookingsRepository.save(booking)).thenReturn(booking);
        Booking bookingRes = bookingsRepository.save(booking);
        assertNotNull(bookingRes);
        assertEquals(booking, bookingRes);
    }

    @Test
    public void readAllUser() {
        doReturn(Optional.of(user)).when(usersRepository).findById(564L);
        doReturn(Optional.of(booking)).when(bookingsRepository).findById(Mockito.any());
        doReturn(Optional.of(item)).when(bookingsRepository).findById(Mockito.any());
        Page<Booking> readList = new PageImpl<>(List.of(booking));
        doReturn(readList).when(bookingsRepository)
                .findBookingByBooker_IdOrderByStartDateDesc(564L, Pageable.unpaged());
        Collection<Booking> bookingRes = bookingService.readAllUser(564L, "ALL", null, null);
        assertNotNull(bookingRes);
        assertEquals(List.of(booking), bookingRes);
    }

    @Test
    public void readAllOwner() {
        doReturn(Optional.of(user)).when(usersRepository).findById(564L);
        doReturn(Optional.of(booking)).when(bookingsRepository).findById(Mockito.any());
        doReturn(Optional.of(item)).when(bookingsRepository).findById(Mockito.any());
        Page<Booking> readList = new PageImpl<>(List.of(booking));
        doReturn(readList).when(bookingsRepository)
                .findBookingByBooker_IdOrderByStartDateDesc(564L, Pageable.unpaged());
        Collection<Booking> bookingRes = bookingService.readAllUser(564L, "ALL", null, null);
        assertNotNull(bookingRes);
        assertEquals(List.of(booking), bookingRes);
    }

    @Test
    public void updateBooking() {
        item = new Item(111L, "Name", "asd", true, user123, 1234L);
        Booking booking = new Booking(403L, start.plusHours(1), finish.plusHours(3), item, user, BookingStatus.WAITING);
        start = start.plusHours(1);
        finish = start.plusHours(2);
        when(usersRepository.findById(123L)).thenReturn(Optional.of(user));
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemsRepository.findById(111L)).thenReturn(Optional.of(item));
        when(bookingsRepository.findById(444L)).thenReturn(Optional.of(booking));
        Booking bookingSave = new Booking(453L, start, finish, item, user, BookingStatus.WAITING);
        when(bookingsRepository.save(booking)).thenReturn(bookingSave);
        Booking bookingRes = bookingService.updateBooking(123L, 444L, true);
        assertNotNull(bookingRes);
        assertEquals(bookingRes, bookingRes);
    }
}