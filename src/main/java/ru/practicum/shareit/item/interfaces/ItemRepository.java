package ru.practicum.shareit.item.interfaces;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> readById(Long itemId);

    Item createItem(Item item);

    Item updateItem(Long itemId, Item item);

    void deleteItem(Long itemId);
        Collection<Item> readAll(Long userId);
}
