package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoImport;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.impl.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingIntegrationTest {
    private final EntityManager em;
    private final BookingServiceImpl bookingService;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;

    @Test
    void create() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        User owner = new User(null, "Owner", "owner@qweqwe.qweqwe");
        User author = new User(null, "Author", "qautort@qweqwe.qweqwe");

        Optional<User> ownerOut = userService.createUser(UserMapper.toDto(owner));
        Optional<User> authorOut = userService.createUser(UserMapper.toDto(author));

        ItemDto itemDto = new ItemDto(1L, "thing", "Description", true, ownerOut.get(), 598L,
                new ArrayList<>(), null, null);
        ItemOutDto itemOut = itemService.createItem(ownerOut.get().getId(), itemDto);

        BookingDtoImport bookingDtoImport = new BookingDtoImport(ownerOut.get().getId(), start, end);
        bookingService.createBooking(authorOut.get().getId(), bookingDtoImport);
        TypedQuery<Booking> query =
                em.createQuery("Select b from Booking b where b.item.id = :id", Booking.class);
        Booking bookingOut = query
                .setParameter("id", itemOut.getId())
                .getSingleResult();
        assertThat(bookingOut.getItem().getId(), equalTo(bookingDtoImport.getItemId()));
        em.clear();
        em.close();
    }
}
