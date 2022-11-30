package javatest.ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingsRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@DataJpaTest
//@AutoConfigureTestDatabase
@SpringBootTest(classes = BookingsRepository.class)
//@Import(BookingsRepository.class)
//@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = BookingsRepository.class))
@Transactional
public class BookingRepositoryTest {
@Autowired
 BookingsRepository bookingsRepository;

    @Test
    public void findBookingByBooker_IdOrderByStartDateDescTest(){

        // Given
        User booker = new User(null, "User name 1", "email@example.com");
        User owner = new User(null, "User name 2", "email2@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = start.plusHours(1);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);
        bookingsRepository.save(booking);
       /* tem.persist(owner);
        tem.persist(booker);
        tem.persist(item);
        tem.persist(booking);*/

        // When
        Page<Booking> result =
                bookingsRepository.findBookingByBooker_IdOrderByStartDateDesc(booker.getId(), Pageable.unpaged());

        // Then
       /* then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));*/
    }
}
