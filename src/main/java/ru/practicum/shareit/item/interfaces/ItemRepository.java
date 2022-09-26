package ru.practicum.shareit.item.interfaces;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> readById(Long itemId);

    void createItem(Item item);

    void updateItem(Long itemId, Item item);

    void deleteItem(Long itemId);

    Collection<Item> readAll(Long userId);

    List<Item> searchText(String text);
}
