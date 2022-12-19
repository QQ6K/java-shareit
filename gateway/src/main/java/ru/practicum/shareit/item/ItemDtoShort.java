package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDtoShort {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private ItemDtoBookingNodes lastBooking;
    private ItemDtoBookingNodes nextBooking;
}
