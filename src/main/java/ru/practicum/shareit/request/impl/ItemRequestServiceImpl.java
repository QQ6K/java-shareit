package ru.practicum.shareit.request.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.item.ItemsRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.interfaces.ItemRequestService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.interfaces.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserService userService;

    private final UsersRepository usersRepository;
    private final ItemRequestRepository itemRequestRepository;

    private ItemsRepository itemsRepository;


    @Transactional
    @Override
    public ItemRequest create(ItemRequestDto itemRequestDto, Long userId) {
        User user = userService.readById(userId);
        if (itemRequestDto.getDescription() == null) {
            throw new BadRequestException("Пустое описание запроса");
        }
        ItemRequest itemRequestNew = ItemRequestMapper.toRequest(itemRequestDto, user, LocalDateTime.now());
        log.info(" Ползователь Создал запрос  userid= {}", userId);
        return itemRequestRepository.save(itemRequestNew);
    }

    @Override
    public ItemRequest findItemRequestById(Long userId, long u) {
        return null;
    }

    @Override
    public List<ItemRequestDto> findAllItemRequestsByOwnerId(Long requesterId) {
        usersRepository.findById(requesterId)
                .orElseThrow(() -> new CrudException("Пользователя не существует", "id", String.valueOf(requesterId)));
        List<ItemRequestDto> itemRequestsDto = new ArrayList<>();
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterId(requesterId);
        itemRequests.forEach(itemRequest -> {
            if (!itemRequest.getRequester().getId().equals(requesterId)) {
                List<ItemRequest> items = itemRequestRepository.findAllByRequesterId(itemRequest.getRequester().getId());
                List<ItemDto> itemsDto = new ArrayList<>();
               // items.forEach(item -> itemsDto.add(ItemMapper.toItemDto(item)));
                itemRequestsDto.add(ItemRequestMapper.toDto(itemRequest, itemsDto));
            }
    });
        return itemRequestsDto;
    }

    @Override
    public List<ItemRequest> getAllItemRequests(Long userId, Integer from, Integer size) {
        if (size == null) {
            return itemRequestRepository.findAllByRequesterId(userId);
        }
        if (size <= 0 || from < 0) {
            throw new BadRequestException("Переданы значения равные нулю или меньше");
        }
        PageRequest pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        Page<ItemRequest> itemRequest = itemRequestRepository.findAllByRequesterIdIsNot(userId, pageable);
        itemRequest.toList();
        return itemRequest.toList();
    }
}
