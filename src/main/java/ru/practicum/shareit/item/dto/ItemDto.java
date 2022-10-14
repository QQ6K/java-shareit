package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDto {
    private Long id;
    @NotEmpty(message = "Пустое поле 'Имя'")
    private String name;
    @NotEmpty(message = "Пустое поле 'Описание'")
    private String description;
    @NotNull(message = "Отсутствует поле 'Статус'")
    private Boolean available;
    private User owner;
    String requestId;
    private Collection<CommentDto> comments;
    private ItemDtoBookingNodes lastBooking;
    private ItemDtoBookingNodes nextBooking;
}
