package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    private Long owner_id;
    String request_id;
}
