package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingsRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.item.CommentsRepository;
import ru.practicum.shareit.item.ItemsRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemsRepository itemRepository;
    private final UsersRepository userRepository;

    private final CommentsRepository commentsRepository;

    private final BookingsRepository bookingsRepository;

    @Override
    public ItemDto readById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CrudException("Вещь не найдена", "id", String.valueOf(itemId)));
        userRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        List<Comment> comments = commentsRepository.findAllByItemId(itemId);
        List<CommentDto> commentDtos = new ArrayList<>(Collections.emptyList());
        if (!comments.isEmpty()) {
            for (Comment comment: comments) {
                CommentDto commentDto1 = CommentMapper.toDto(comment);
                commentDtos.add(commentDto1);
            }
        }
        List<Booking> bookingLast = bookingsRepository.findLastBooking(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());
        List<Booking> bookingNext = bookingsRepository.findNextBooking(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());
        ItemDtoBookingNodes itemDtoBookingNodesLast;
        ItemDtoBookingNodes itemDtoBookingNodesNext;
        if (!bookingNext.isEmpty()) {
            itemDtoBookingNodesNext = new ItemDtoBookingNodes(bookingNext.get(0).getId(),bookingNext.get(0).getBooker().getId());
        } else itemDtoBookingNodesNext = null;
        if (!bookingLast.isEmpty()) {
            itemDtoBookingNodesLast = new ItemDtoBookingNodes(bookingLast.get(0).getId(),bookingLast.get(0).getBooker().getId());
        } else itemDtoBookingNodesLast = null;
        ItemDto itemDto = ItemMapper.toItemBookingDto(item, commentDtos, itemDtoBookingNodesLast, itemDtoBookingNodesNext);
        log.info("Просмотр вещи  id = {} пользователем id = {}", itemDto, userId);
        return itemDto;
    }

    @Override
    public Collection<ItemDtoShort> readAll(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        List<Item> items = itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
        List<ItemDtoShort> resultItems = new ArrayList<>(Collections.emptyList());
        for (Item item: items) {
            List<Booking> bookingLast = bookingsRepository.findLastBooking(item.getId(), userId, BookingStatus.APPROVED, LocalDateTime.now());
            List<Booking> bookingNext = bookingsRepository.findNextBooking(item.getId(), userId, BookingStatus.APPROVED, LocalDateTime.now());
            ItemDtoBookingNodes itemDtoBookingNodesLast;
            ItemDtoBookingNodes itemDtoBookingNodesNext;
            if (!bookingNext.isEmpty()) {
                itemDtoBookingNodesNext = new ItemDtoBookingNodes(bookingNext.get(0).getId(), bookingNext.get(0).getBooker().getId());
            } else itemDtoBookingNodesNext = null;
            if (!bookingLast.isEmpty()) {
                itemDtoBookingNodesLast = new ItemDtoBookingNodes(bookingLast.get(0).getId(), bookingLast.get(0).getBooker().getId());
            } else itemDtoBookingNodesLast = null;

            ItemDtoShort itemDtoShort = ItemMapper.toItemDtoShort(item, itemDtoBookingNodesLast,itemDtoBookingNodesNext);
            resultItems.add(itemDtoShort);
        }
        log.info("Просмотр вещей пользователем id = {}", userId);
        return resultItems;
    }

    @Override
    @Transactional
    public ItemOutDto createItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует",
                "id", String.valueOf(userId)));
        Item item = ItemMapper.fromDto(itemDto);
        item.setOwner(user);
        itemRepository.save(item);
        log.info("Создание вещи  id: {}", item.getId());
        Optional<Item> itemReturn = itemRepository.findById(item.getId());
        return ItemMapper.toItemOutDto(itemReturn.get());
    }

    @Override
    @Transactional
    public Optional<Item> updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item updateItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new CrudException("Вещи не существует",
                        "id", String.valueOf(itemDto.getId())));
        if (!updateItem.getOwner().getId().equals(userId)) {
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
        if (itemDto.getAvailable() != null) {
            updateItem.setAvailable(itemDto.getAvailable());
        }
        itemRepository.save(updateItem);
        log.info("Просмотр вещей пользователем id = {}", userId);
        return itemRepository.findById(itemId);
    }

    @Override
    @Transactional
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
                    .filter((Item::getAvailable))
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                            || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
            log.info("Поиск текста: {}", text);
            return searchResult;
        } else {
            log.info("Пустой результат поиска текста: {}", text);
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public CommentDto addComment(Long itemId, long userId, String text) {
        if (text.equals("")) {
            throw new BadRequestException("Пустой комментарий");
        }
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new CrudException("Вещи не существует", "id", String.valueOf(itemId)));
        User user = userRepository.findById(userId).orElseThrow(() -> new CrudException("Пользователя не существует", "id", String.valueOf(userId)));
        int l = bookingsRepository.usedCount(userId, itemId, BookingStatus.APPROVED, LocalDateTime.now());
        if (l > 0) {
            log.info("Пользователь id = {}. Вещь = {}. Сохранение комментария: {}", itemId, userId, text);
            return CommentMapper.toDto(commentsRepository.save(new Comment(0, text, item, user, LocalDateTime.now())));
        } else {
            throw new BadRequestException("Без бронирования нельзя оставить отзыв id = " + itemId);
        }
    }


}
