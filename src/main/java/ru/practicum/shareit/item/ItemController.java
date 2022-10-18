package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.interfaces.ItemService;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Optional<Item> saveItem(@Valid @RequestHeader("X-Sharer-User-Id")
                                  Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Запрос 'POST /items'");
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Optional<Item> patchItem(@Valid @NotNull(message = "Отсутсвует X-Sharer-User-Id") @RequestHeader("X-Sharer-User-Id")
                                    long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("Запрос 'PATCH /items/{}'", itemId);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteUser(@PathVariable long itemId) {
        log.info("Запрос 'DELETE /items/{}'", itemId);
        itemService.deleteItem(itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto readById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Запрос 'GET /items/{}'", itemId);
        return itemService.readById(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDtoShort> readAll(@RequestHeader("X-Sharer-User-Id") @NotNull(message = "Отсутсвует X-Sharer-User-Id")
                                    long userId) {
        log.info("Запрос 'GET /items' пользователя " + userId);
        return itemService.readAll(userId);
    }

    @GetMapping("/search")
    public Collection<Item> findByText(@RequestParam String text) {
        log.info("Запрос 'GET /search?text={}'", text);
        return itemService.searchText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable("itemId") Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody @Valid CommentDto commentDto) {
        Comment comment = itemService.addComment(itemId, userId, commentDto.getText());
        return CommentMapper.toDto(comment);
    }
}
