package ru.practicum.shareit.item.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.interfaces.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> itemRepository = new HashMap<>();
    private long id = 1;

    @Override
    public Optional<Item> readById(Long itemId) {
        return itemRepository.keySet().stream().filter(i -> i == itemId).findAny().map(itemRepository::get);
    }

    @Override
    public Item createItem(Item item) {
        item.setId(id);
        id++;
        itemRepository.put(item.getId(), item);
        return itemRepository.get(item.getId());
    }

    @Override
    public Item updateItem(Long itemId, Item item) {
        itemRepository.put(itemId, item);
        return itemRepository.get(item.getId());
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.remove(itemId);
    }

    @Override
    public Collection<Item> readAll(Long userId) {
        return itemRepository.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }
}
