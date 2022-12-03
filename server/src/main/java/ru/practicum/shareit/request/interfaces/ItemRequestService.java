package ru.practicum.shareit.request.interfaces;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequest create(ItemRequestDto itemRequestDto, Long userId);

    ItemRequestItemsDto findItemRequestById(Long userId, Long requestId);

    List<ItemRequestItemsDto> findAllItemRequestsByOwnerId(Long requesterId);

    List<ItemRequestItemsDto> getAllItemRequests(Long userId, Integer from, Integer size);
}
