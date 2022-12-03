package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemService {

    Optional<Item> updateItem(Long userId, Long itemId, ItemDto itemDto);

    void deleteItem(Long itemId);

    ItemDto readById(Long itemId, Long userId);

    Collection<ItemDtoShort> readAll(Long userId);

    ItemOutDto createItem(Long userId, ItemDto itemDto);

    List<Item> searchText(String text);

    @Transactional
    CommentDto addComment(Long itemId, long userId, String text);
}
