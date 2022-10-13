package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

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
