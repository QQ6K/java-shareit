package ru.practicum.shareit.request.interfaces;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequest create(ItemRequestDto itemRequestDto, Long userId);

    ItemRequest  findItemRequestById(Long userId, long u);

    List<ItemRequestDto> findAllItemRequestsByOwnerId(Long requesterId);

    List<ItemRequest> getAllItemRequests(Long userId, Integer from, Integer size);
}
