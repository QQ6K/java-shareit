package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingsRepository extends JpaRepository<Booking,Long> {
    //Collection<Booking> findByBooker_IdOrderByStart_dateDesc(Long id);

}
