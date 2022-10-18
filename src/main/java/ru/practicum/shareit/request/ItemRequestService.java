package ru.practicum.shareit.request;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest create(ItemRequest itemRequest, Long userId);

    List<ItemRequest> findAllItemRequestsByOwnerId(Long requesterId);

    ItemRequest findItemRequestById(Long requestId, Long requesterId);

    List<ItemRequest> getAllItemRequests(Long userId, Integer from, Integer size);
}
