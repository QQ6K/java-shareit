package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/requests")
public class ItemRequestController {

    private final ItemRequestServiceImpl itemRequestServiceImpl;

    @PostMapping
    public ItemRequest createNewRequest(@RequestBody ItemRequestDto itemRequestDto,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestServiceImpl.create(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllRequestsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestServiceImpl.findAllItemRequestsByOwnerId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequest> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(name = "from", required = false) Integer from,
                                               @RequestParam(name = "size", required = false) Integer size) {
        return itemRequestServiceImpl.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequest findRequestInfoById(@PathVariable("requestId") Long requestId,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestServiceImpl.findItemRequestById(requestId, userId);
    }
}
