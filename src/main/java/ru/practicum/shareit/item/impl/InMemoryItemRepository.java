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
        return itemRepository.keySet().stream().filter(i -> Objects.equals(i, itemId)).findAny().map(itemRepository::get);
    }

    @Override
    public void createItem(Item item) {
        item.setId(id);
        id++;
        itemRepository.put(item.getId(), item);
        itemRepository.get(item.getId());
    }

    @Override
    public void updateItem(Long itemId, Item item) {
        itemRepository.put(itemId, item);
        itemRepository.get(item.getId());
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

    @Override
    public List<Item> searchText(String text) {
        List<Item> searchResult;
        if (!text.isEmpty()) {
            searchResult = itemRepository.values().stream()
                    .filter((Item::getAvailable))
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                            || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
            return searchResult;
        } else return Collections.emptyList();
    }
}
