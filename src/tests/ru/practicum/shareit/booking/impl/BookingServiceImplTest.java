package ru.practicum.shareit.booking.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.practicum.shareit.booking.BookingsRepository;
import ru.practicum.shareit.booking.dto.BookingDtoImport;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.ItemsRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;

    @MockBean
    private ItemsRepository itemsRepository;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private BookingsRepository bookingsRepository;

    User user  = new User(1L,"qwe@qwe.qwe","qwe@qwe.qwe");

    Item item = new Item(111L,"Name","asd",true,user,1234L);

    Booking booking = new Booking(403L, LocalDateTime.now(),LocalDateTime.now(),item,user, BookingStatus.APPROVED);

    @Test
    public void createBookingTest() {
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemsRepository.findById(111L)).thenReturn(Optional.of(item));
        when(bookingsRepository.save(booking)).thenReturn(booking);
        BookingDtoImport bookingDtoImport = new BookingDtoImport(208L,LocalDateTime.now(),LocalDateTime.now());
        Booking bookingRes = bookingService.createBooking(1L,bookingDtoImport);
        assertNotNull(bookingRes);
        assertEquals(booking, bookingRes);
    }

    @Test
    public void readById() {
        doReturn(Optional.of(new User(1L,"Test","qweqwe@qwe.qwe")))
                .when(usersRepository).findById(Mockito.any());
        doReturn(Optional.of(booking)).when(bookingsRepository).findById(Mockito.any());
        doReturn(Optional.of(item)).when(bookingsRepository).findById(Mockito.any());
        when(bookingsRepository.save(booking)).thenReturn(booking);
        Booking bookingRes = bookingsRepository.save(booking);

    }

    @Test
    public void readAllUser() {
    }

    @Test
    public void readAllOwner() {
    }

    @Test
    public void updateBooking() {
    }
}