package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.interfaces.ItemRepository;
import ru.practicum.shareit.item.interfaces.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item readById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new CrudException("Вещь не найдена", "id", String.valueOf(itemId)));
    }

    @Override
    public Collection<Item> readAll(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        return itemRepository.findAll().stream()
                .filter(item -> item.getOwner_id().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> createItem(Long userId, ItemDto itemDto) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new CrudException("Пользователя не существует",
                    "id", String.valueOf(userId));
        }
        userRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        Item item = ItemMapper.fromDto(itemDto);
        item.setOwner_id(userId);
        itemRepository.save(item);
        log.info("Создание вещи  id: {}", item.getId());
        return itemRepository.findById(item.getId());
    }

    @Override
    public Optional<Item> updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item updateItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new CrudException("Вещи не существует",
                        "id", String.valueOf(itemDto.getId())));
        if (!updateItem.getOwner_id().equals(userId)) {
            throw
                    new CrudException("У пользователя отсутствуют права на изменения характеристик вещи",
                            "Пользователь " + userId, "вещь " + itemId);
        }
        if (itemDto.getName() != null) {
            updateItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            updateItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getIs_available() != null) {
            updateItem.setIs_available(itemDto.getIs_available());
        }
        itemRepository.save(updateItem);
        return itemRepository.findById(itemId);
    }

    @Override
    public void deleteItem(Long itemId) {
        if (itemRepository.findById(itemId).isPresent()) {
            itemRepository.deleteById(itemId);
            log.info("Удалена вещь  id: {}", itemId);

        } else {
            throw new CrudException("Вещь с таким id не существует", "id", String.valueOf(itemId));
        }
    }

    @Override
    public List<Item> searchText(String text) {
        List<Item> searchResult;
        if (!text.isEmpty()) {
            searchResult = itemRepository.findAll().stream()
                    .filter((Item::getIs_available))
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                            || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
            return searchResult;
        }else return Collections.emptyList();
    }
}