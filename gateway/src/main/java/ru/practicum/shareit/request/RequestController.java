package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Запрос 'POST /requests' пользователя " + requestorId);
        return requestClient.create(itemRequestDto, requestorId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable("requestId") Long itemRequestId,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос 'GET /requests' пользователя " + userId);
        return requestClient.getItemRequestById(userId, itemRequestId);
    }


    @GetMapping
    public ResponseEntity<Object> getOwnItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос 'GET /requests' пользователя " + userId);
        return requestClient.getOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                     Integer from,
                                                     @RequestParam(required = false) Integer size) {
        log.info("Запрос 'GET /requests' пользователя " + userId);
        return requestClient.getAllItemRequests(userId, from, size);
    }

}
