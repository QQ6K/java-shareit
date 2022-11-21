package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest toRequest(ItemRequestDto itemRequestDto, User requester, LocalDateTime now) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequester(requester);
        itemRequest.setCreated(now);
        return itemRequest;
    }

    public static ItemRequestDto toDto(ItemRequest itemRequest, List<ItemDto> items) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequester().getId(),
                itemRequest.getCreated(),
                items);
    }
}
