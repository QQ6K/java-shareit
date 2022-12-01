package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoImport;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.impl.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingIntegrationTest {
    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final BookingServiceImpl bookingService;

    @Test
    void create() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        User owner = new User(1L, "Owner", "owner@qweqwe.qweqwe");
        User author = new User(2L, "Author", "qautort@qweqwe.qweqwe");
        User requester = new User(3L, "Name", "rekvester9000@qweqwe.qweqwe");

        em.merge(owner);
        em.merge(requester);
        em.merge(author);

        Item item = new Item(1L, "thing", "Description", true, owner, requester.getId());
        em.merge(item);

        BookingDtoImport bookingDtoImport = new BookingDtoImport(1L, start, end);
        UserDto userDto = new UserDto(2L, "Name", "qweqwe@qweqwe.qweqwe");
        bookingService.createBooking(userDto.getId(), bookingDtoImport);
        TypedQuery<Booking> query =
                em.createQuery("Select b from Booking b where b.item.id = :id", Booking.class);
        Booking bookingOut = query
                .setParameter("id", item.getId())
                .getSingleResult();
        assertThat(bookingOut.getItem().getId(), equalTo(bookingDtoImport.getItemId()));
    }
}
