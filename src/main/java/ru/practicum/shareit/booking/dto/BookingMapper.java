package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    public static BookingDto toDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart_date(),
                booking.getEnd_date(),
                booking.getItem_id(),
                booking.getBooker_id(),
                booking.getStatus());

    }

    public static Booking fromDto(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItemId(),
                bookingDto.getBooker_id(),
                bookingDto.getStatus());
    }
}
