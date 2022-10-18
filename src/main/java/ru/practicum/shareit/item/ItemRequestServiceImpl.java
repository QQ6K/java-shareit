package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.exceptions.EmptyUserValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.interfaces.ItemRepository;
import ru.practicum.shareit.item.interfaces.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.interfaces.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item readById(Long itemId) {
        return itemRepository.readById(itemId)
                .orElseThrow(() -> new CrudException("Вещь не найдена", "id", String.valueOf(itemId)));
    }

    @Override
    public Collection<Item> readAll(Long userId) {
        userRepository.readById(userId).orElseThrow(() -> new EmptyUserValidException("Пользователя не существует",
                "id", String.valueOf(userId)));
        return itemRepository.readAll(userId);
    }

    @Override
    public Optional<Item> createItem(Long userId, ItemDto itemDto) {
        if (userRepository.readById(userId).isEmpty()) {
            throw new EmptyUserValidException("Пользователя не существует",
                    "id", String.valueOf(userId));
        }
        userRepository.readById(userId).orElseThrow(() -> new EmptyUserValidException("Пользователя не существует",
                "id", String.valueOf(userId)));
        Item item = ItemMapper.fromDto(itemDto);
        item.setOwner(userId);
        itemRepository.createItem(item);
        log.info("Создание вещи  id: {}", item.getId());
        return itemRepository.readById(item.getId());
    }

    @Override
    public Optional<Item> updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item updateItem = itemRepository.readById(itemId)
                .orElseThrow(() -> new CrudException("Вещи не существует",
                        "id", String.valueOf(itemDto.getId())));
        if (!updateItem.getOwner().equals(userId)) {
            throw
                    new EmptyUserValidException("У пользователя отсутствуют права на изменения характеристик вещи",
                            "Пользователь " + userId, "вещь " + itemId);
        }
        if (itemDto.getName() != null) {
            updateItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            updateItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updateItem.setAvailable(itemDto.getAvailable());
        }
        itemRepository.updateItem(itemId, updateItem);
        return itemRepository.readById(itemId);
    }

    @Override
    public void deleteItem(Long itemId) {
        if (itemRepository.readById(itemId).isPresent()) {
            itemRepository.deleteItem(itemId);
            log.info("Удалена вещь  id: {}", itemId);

        } else {
            throw new CrudException("Вещь с таким id не существует", "id", String.valueOf(itemId));
        }
    }

    @Override
    public List<Item> searchText(String text) {
        return itemRepository.searchText(text);
    }
}
