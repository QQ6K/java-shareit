package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingsRepository extends JpaRepository<Booking, Long> {


        Collection<Booking> findBookingByBooker_IdOrderByStartDateDesc(Long id);

        Collection<Booking> findByBooker_IdAndStatus(long id, BookingStatus status);

        @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE i.owner.id = :ownerId ORDER BY b.startDate DESC")
        Collection<Booking> findOwnerAll(long ownerId);

        @Query("SELECT b FROM Booking b " +
                "WHERE b.booker.id=:id AND b.endDate<:nowTime AND b.status =:status " +
                "ORDER BY b.startDate DESC")
        Collection<Booking> findByBookerIdStatePast(@Param("id") Long id,
                                                    @Param("nowTime") LocalDateTime nowTime,
                                                    @Param("status") BookingStatus status);

        @Query("SELECT b FROM Booking b WHERE b.booker.id=:userId AND b.endDate >= :nowTime AND :nowTime >= b.startDate " +
                "ORDER BY b.startDate DESC")
        Collection<Booking> findByBookerIdStateCurrent(@Param("userId") Long useId, @Param("nowTime") LocalDateTime nowTime);

        @Query(value = "SELECT b FROM Booking b WHERE b.booker.id =:userId AND " +
                "b.startDate > :dateNow ORDER BY b.startDate DESC")
        Collection<Booking> findFuture(@Param("userId") Long useId, @Param("dateNow") LocalDateTime dateNow);

        //  Collection<Booking> findByBooker_IdAndStatus(long id, BookingStatus status);

        //  Collection<Booking> findAllByBooker_IdAndState

        @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE i.owner.id = :ownerId ORDER BY b.startDate DESC")
        Collection<Booking> findOwnerAll(Long ownerId);

        Collection<Booking> findByItem_OwnerOrderByStartDateDesc(Long ownerId);

        @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE  i.owner.id = :userId AND b.startDate> :timeNow " +
                "ORDER BY b.startDate DESC")
        Collection<Booking> findOwnerFuture(@Param("userId") Long userId, @Param("timeNow") LocalDateTime timeNow);

        @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE i.owner.id = :userId " +
                "AND b.startDate <= :timeNow AND b.endDate >= :timeNow ORDER BY b.startDate DESC ")
        Collection<Booking> findOwnerCurrent(@Param("userId") Long userId, @Param("timeNow") LocalDateTime imeNow);

        @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE i.owner.id = :userId AND b.status = :status")
        Collection<Booking> findByOwnerIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status);

        @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE i.owner.id = :userId AND b.endDate< :timeNow")
        Collection<Booking> findOwnerPast(@Param("userId") Long userId, @Param("timeNow") LocalDateTime timeNow);

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

        @Query("SELECT b FROM Booking b WHERE b.item.owner.id =:ownerId AND b.status =:status AND b.startDate>:timeNow ORDER BY b.startDate ASC")
        List<Booking> findNextBookingItemUser(@Param("ownerId") Long ownerId,
                                      @Param("status") BookingStatus status,
                                      @Param("timeNow") LocalDateTime timeNow);

        @Query("SELECT b FROM Booking b JOIN b.item i ON b.item = i WHERE i.owner.id =:ownerId AND b.status =:status AND b.startDate>:timeNow ORDER BY b.endDate ASC")
        List<Booking>  findLastBookingItemUser(@Param("ownerId") Long ownerId,
                                       @Param("status") BookingStatus status,
                                       @Param("timeNow") LocalDateTime timeNow);

        @Query("SELECT b FROM Booking b WHERE b.item.id=:itemId AND b.booker.id=:userId")
        Booking  findTestBooking(@Param("itemId") Long itemId, @Param("userId") Long userId);



}
