package ru.practicum.shareit.item.interfaces;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemService {

    Optional<Item> updateItem(Long userId, Long itemId, ItemDto itemDto);

    void deleteItem(Long itemId);

    ItemDto readById(Long itemId, Long userId);

    Collection<Item> readAll(Long userId);

    Optional<Item> createItem(Long userId, ItemDto itemDto);

    List<Item> searchText(String text);

    @Transactional
    Comment addComment(Long itemId, long userId, String text);
}
