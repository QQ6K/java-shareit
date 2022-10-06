package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static BookingDtoExport toDto(Booking booking, User booker, Item item) {
        return new BookingDtoExport(
                booking.getId(),
                booking.getStart_date(),
                booking.getEnd_date(),
                booker,
                item,
                booking.getStatus());

    }
}
