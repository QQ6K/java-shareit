package ru.practicum.shareit.item.interfaces;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {

    Optional<Item> updateItem(Long userId, Long itemId, ItemDto itemDto);

    void deleteItem(Long itemId);
    Item readById(Long itemId);

     Collection<Item> readAll(Long userId);

    Optional<Item> createItem(Long userId, ItemDto itemDto);

}
