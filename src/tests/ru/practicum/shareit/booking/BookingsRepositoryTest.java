package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
public class BookingsRepositoryTest {
    @Autowired
    TestEntityManager tem;
    @Autowired
    BookingsRepository bookingsRepository;

    @Test
    public void findBookingByBooker_IdOrderByStartDateDescTest(){
        User booker = new User(null, "Name1", "qwe@qwe.qwe");
        User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
        Item item = new Item(null, "name", "description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = start.plusHours(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);
        Page<Booking> result =
                bookingsRepository.findBookingByBooker_IdOrderByStartDateDesc(booker.getId(), Pageable.unpaged());
        assertEquals(List.of(result).size(),1);
    }
@Test
    public void findByBooker_IdAndStatusTest(){
    User booker = new User(null, "Name1", "qwe@qwe.qwe");
    User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
    Item item = new Item(null, "name", "description", true, owner, null);
    final LocalDateTime start = LocalDateTime.now();
    final LocalDateTime end = start.plusHours(1);
    Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
    tem.persist(owner);
    tem.persist(booker);
    tem.persist(item);
    tem.persist(booking);
    List<Booking> result =
            bookingsRepository.findByBooker_IdAndStatus(booker.getId(),BookingStatus.WAITING,Pageable.unpaged()).getContent();
    assertEquals(result.size(),1);
}

    @Test
    public void findByBookerIdStatePastTest(){
        User booker = new User(null, "Name1", "qwe@qwe.qwe");
        User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
        Item item = new Item(null, "name", "description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now().minusDays(1);
        final LocalDateTime end = start.plusHours(8);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);
        List<Booking> result =
                bookingsRepository.findByBookerIdStatePast(booker.getId(),LocalDateTime.now(),BookingStatus.WAITING,Pageable.unpaged()).getContent();
        assertEquals(result.size(),1);
    }



    @Test
    public void findByBookerIdStateCurrentTest(){
        User booker = new User(null, "Name1", "qwe@qwe.qwe");
        User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
        Item item = new Item(null, "name", "description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now().minusHours(1);
        final LocalDateTime end = start.plusHours(2);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);
        List<Booking> result =
                bookingsRepository.findByBookerIdStateCurrent(booker.getId(),LocalDateTime.now(),Pageable.unpaged()).getContent();
        assertEquals(result.size(),1);
    }
    @Test
    public void findFutureTest(){
        User booker = new User(null, "Name1", "qwe@qwe.qwe");
        User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
        Item item = new Item(null, "name", "description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now().plusHours(1);
        final LocalDateTime end = start.plusHours(2);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);
        List<Booking> result =
                bookingsRepository.findFuture(booker.getId(),LocalDateTime.now(),Pageable.unpaged()).getContent();
        assertEquals(result.size(),1);
    }

    @Test
    public void findOwnerAllTest(){
        User booker = new User(null, "Name1", "qwe@qwe.qwe");
        User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
        Item item = new Item(null, "name", "description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now().plusHours(1);
        final LocalDateTime end = start.plusHours(2);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);
        List<Booking> result =
                bookingsRepository.findOwnerAll(item.getOwner().getId(),Pageable.unpaged()).getContent();
        assertEquals(result.size(),1);
    }

    @Test
    public void findOwnerFutureTest(){
        User booker = new User(null, "Name1", "qwe@qwe.qwe");
        User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
        Item item = new Item(null, "name", "description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now().plusHours(1);
        final LocalDateTime end = start.plusHours(2);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);
        List<Booking> result =
                bookingsRepository.findOwnerFuture(item.getOwner().getId(),LocalDateTime.now(),Pageable.unpaged()).getContent();
        assertEquals(result.size(),1);
    }

    @Test
    public void findOwnerCurrentTest(){
        User booker = new User(null, "Name1", "qwe@qwe.qwe");
        User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
        Item item = new Item(null, "name", "description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now().minusHours(1);
        final LocalDateTime end = start.plusHours(2);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);
        List<Booking> result =
                bookingsRepository.findOwnerCurrent(item.getOwner().getId(),LocalDateTime.now(),Pageable.unpaged()).getContent();
        assertEquals(result.size(),1);
    }

    @Test
    public void findByOwnerIdAndStatusTest(){
        User booker = new User(null, "Name1", "qwe@qwe.qwe");
        User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
        Item item = new Item(null, "name", "description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now().minusHours(1);
        final LocalDateTime end = start.plusHours(2);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);
        List<Booking> result =
                bookingsRepository.findByOwnerIdAndStatus(item.getOwner().getId(),BookingStatus.WAITING,Pageable.unpaged()).getContent();
        assertEquals(result.size(),1);
    }

    @Test
    public void findOwnerPastTest(){
        User booker = new User(null, "Name1", "qwe@qwe.qwe");
        User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
        Item item = new Item(null, "name", "description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now().minusHours(4);
        final LocalDateTime end = start.plusHours(2);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);
        List<Booking> result =
                bookingsRepository.findOwnerPast(item.getOwner().getId(),LocalDateTime.now(),Pageable.unpaged()).getContent();
        assertEquals(result.size(),1);
    }

    @Test
    public void usedCountTest(){
        User booker = new User(null, "Name1", "qwe@qwe.qwe");
        User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
        Item item = new Item(null, "name", "description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now().minusHours(5);
        final LocalDateTime end = start.plusHours(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.APPROVED);
        tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);
        int result =
                bookingsRepository.usedCount(booker.getId(),item.getId(),BookingStatus.APPROVED,LocalDateTime.now());
        assertEquals(result,1);
    }

    @Test
    public void findNextBookingTest(){
        User booker = new User(null, "Name1", "qwe@qwe.qwe");
        User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
        Item item = new Item(null, "name", "description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now().plusHours(5);
        final LocalDateTime end = start.plusHours(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.APPROVED);
        tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);
        List<Booking> result =
                bookingsRepository.findNextBooking(item.getId(),item.getOwner().getId(),BookingStatus.APPROVED,LocalDateTime.now());
        assertEquals(result.size(),1);
    }


    @Test
    public void findLastBookingTest(){
        User booker = new User(null, "Name1", "qwe@qwe.qwe");
        User owner = new User(null, "Name2", "qweqwe@qwe.qwe");
        Item item = new Item(null, "name", "description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now().minusHours(5);
        final LocalDateTime end = start.plusHours(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.APPROVED);
        tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);
        List<Booking> result =
                bookingsRepository.findLastBooking(item.getId(),item.getOwner().getId(),BookingStatus.APPROVED,LocalDateTime.now());
        assertEquals(result.size(),1);
    }
}
