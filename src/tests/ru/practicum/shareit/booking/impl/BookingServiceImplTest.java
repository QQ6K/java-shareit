package ru.practicum.shareit.booking.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.practicum.shareit.booking.BookingsRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.ItemsRepository;
import ru.practicum.shareit.item.model.Item;
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

    @MockBean
    private ItemsRepository itemsRepository;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private BookingsRepository bookingsRepository;

    User user  = new User();

    Item item = new Item();

    Booking booking = new Booking(403L, LocalDateTime.now(),LocalDateTime.now(),item,user, BookingStatus.APPROVED);

    @Test
    public void createBooking() {
        when(usersRepository.findById(789L)).thenReturn(Optional.of(user));
        when(itemsRepository.findById(3561L)).thenReturn(Optional.of(item));
        when(bookingsRepository.save(booking)).thenReturn(booking);
        item.setAvailable(false);
        Booking bookingRes = bookingsRepository.save(booking);
        assertNotNull(bookingRes);
        assertEquals(booking, bookingRes);
    }

    @Test
    public void readById() {
        doReturn(Optional.of(new User(1L,"Test","qweqwe@qwe.qwe")))
                .when(usersRepository).findById(Mockito.any());

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