package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    @Override
    public ItemRequest create(ItemRequest itemRequest, Long userId) {
        return null;
    }

    @Override
    public List<ItemRequest> findAllItemRequestsByOwnerId(Long requesterId) {
        return null;
    }

    @Override
    public ItemRequest findItemRequestById(Long requestId, Long requesterId) {
        return null;
    }

    @Override
    public List<ItemRequest> getAllItemRequests(Long userId, Integer from, Integer size) {
        return null;
    }
}
