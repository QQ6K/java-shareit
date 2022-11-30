package ru.practicum.shareit.booking;

import java.time.LocalDateTime;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@ContextConfiguration(classes = {BookingsRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"ru.practicum.shareit.booking.model"})
@DataJpaTest
public class BookingsRepositoryTest {
    @Autowired
    private BookingsRepository bookingsRepository;

}

