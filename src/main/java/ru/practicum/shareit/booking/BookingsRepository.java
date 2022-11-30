package ru.practicum.shareit.booking;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface BookingsRepository extends JpaRepository<Booking, Long> {


        Page<Booking> findBookingByBooker_IdOrderByStartDateDesc(Long id, Pageable pageable);

        Page<Booking> findByBooker_IdAndStatus(long id, BookingStatus status, Pageable pageable);

        @Query("SELECT b FROM Booking b " +
                "WHERE b.booker.id=:id AND b.endDate<:nowTime AND b.status =:status " +
                "ORDER BY b.startDate DESC")
        Page<Booking> findByBookerIdStatePast(@Param("id") Long id,
                                                    @Param("nowTime") LocalDateTime nowTime,
                                                    @Param("status") BookingStatus status, Pageable pageable);

        @Query("SELECT b FROM Booking b WHERE b.booker.id=:userId AND b.endDate >= :nowTime AND :nowTime >= b.startDate " +
                "ORDER BY b.startDate DESC")
        Page<Booking> findByBookerIdStateCurrent(@Param("userId") Long useId, @Param("nowTime") LocalDateTime nowTime, Pageable pageable);

        @Query(value = "SELECT b FROM Booking b WHERE b.booker.id =:userId AND " +
                "b.startDate > :dateNow ORDER BY b.startDate DESC")
        Page<Booking> findFuture(@Param("userId") Long useId, @Param("dateNow") LocalDateTime dateNow, Pageable pageable);

        @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE i.owner.id = :ownerId ORDER BY b.startDate DESC")
        Page<Booking> findOwnerAll(Long ownerId, Pageable pageable);

        @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE  i.owner.id = :userId AND b.startDate> :timeNow " +
                "ORDER BY b.startDate DESC")
        Page<Booking> findOwnerFuture(@Param("userId") Long userId, @Param("timeNow") LocalDateTime timeNow, Pageable pageable);

        @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE i.owner.id = :userId " +
                "AND b.startDate <= :timeNow AND b.endDate >= :timeNow ORDER BY b.startDate DESC ")
        Page<Booking> findOwnerCurrent(@Param("userId") Long userId, @Param("timeNow") LocalDateTime timeNow, Pageable pageable);

        @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE i.owner.id = :userId AND b.status = :status")
        Page<Booking> findByOwnerIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status, Pageable pageable);

        @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE i.owner.id = :userId AND b.endDate< :timeNow")
        Page<Booking> findOwnerPast(@Param("userId") Long userId, @Param("timeNow") LocalDateTime timeNow,Pageable pageable);

        @Query("SELECT count (b.id) FROM Booking b" +
                " WHERE b.booker.id =:userId AND b.item.id=:itemId AND b.status=:status AND b.startDate<:timeNow ")
        int usedCount(@Param("userId") Long userId, @Param("itemId") Long itemId, @Param("status") BookingStatus status,
                      @Param("timeNow") LocalDateTime timeNow);

        @Query("SELECT b FROM Booking b WHERE b.item.id =:itemId AND b.item.owner.id =:ownerId AND b.status =:status AND b.startDate>:timeNow ORDER BY b.startDate ASC")
        List<Booking> findNextBooking(@Param("itemId") Long itemId, @Param("ownerId") Long ownerId,
                                     @Param("status") BookingStatus status, @Param("timeNow") LocalDateTime timeNow);

        @Query("SELECT b FROM Booking b WHERE b.item.id =:itemId AND b.item.owner.id =:ownerId AND b.status =:status AND b.endDate<:timeNow ORDER BY b.endDate DESC")
        List<Booking>  findLastBooking(@Param("itemId") Long itemId, @Param("ownerId") Long ownerId,
                                @Param("status") BookingStatus status,@Param("timeNow") LocalDateTime timeNow);


}
