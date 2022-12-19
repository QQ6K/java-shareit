package ru.practicum.shareit.request.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.exceptions.WrongUserException;
import ru.practicum.shareit.item.ItemsRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.interfaces.ItemRequestService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UsersRepository usersRepository;

    private final ItemRequestRepository itemRequestRepository;

    private final ItemsRepository itemsRepository;


    @Transactional
    @Override
    public ItemRequest create(ItemRequestDto itemRequestDto, Long userId) {
        User user = usersRepository.findById(userId).orElseThrow(() ->
                new WrongUserException("Пользователя не существует id = " + userId));
        if (itemRequestDto.getDescription() == null) {
            throw new BadRequestException("Пустое описание запроса");
        }
        ItemRequest itemRequestNew = ItemRequestMapper.toItemRequest(itemRequestDto, user, LocalDateTime.now());
        log.info("Пользователь Создал запрос  userid = {}", userId);
        return itemRequestRepository.save(itemRequestNew);
    }

    @Override
    public ItemRequestItemsDto findItemRequestById(Long requestId, Long userId) {
        User user = usersRepository.findById(userId).orElseThrow(() ->
                new WrongUserException("Пользователя не существует id = " + userId));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() ->
                new WrongUserException("Запрос не существует id = " + requestId));
        ItemRequestDto itemRequestDto  = ItemRequestMapper.toDto(itemRequest);
        List<ItemOutDto> itemDtos = itemsRepository
                .findAllByRequestId(requestId)
                .stream().map(ItemMapper::toItemOutDto).collect(Collectors.toList());
        return ItemRequestMapper.toDtoItems(ItemRequestMapper.toRequest(itemRequestDto, user),itemDtos);
    }

    @Override
    public List<ItemRequestItemsDto> findAllItemRequestsByOwnerId(Long ownId) {
        usersRepository.findById(ownId)
                .orElseThrow(() -> new CrudException("Пользователя не существует", "id", String.valueOf(ownId)));
        List<ItemRequestItemsDto> itemRequestsItemsDto = new ArrayList<>();
        List<ItemRequestDto> itemRequests = itemRequestRepository.findAllByRequesterIdOrderByCreatedAsc(ownId)
                .stream().map(ItemRequestMapper::toDto).collect(Collectors.toList());
        for (ItemRequestDto itemRequestDto: itemRequests) {
            List<ItemOutDto> itemDtos = itemsRepository
                    .findAllByRequestId(itemRequestDto.getId())
                    .stream().map(ItemMapper::toItemOutDto).collect(Collectors.toList());
            User user = usersRepository.getOne(itemRequestDto.getRequesterId());
            ItemRequestItemsDto itemRequestItemDto =
                    ItemRequestMapper.toDtoItems(ItemRequestMapper.toRequest(itemRequestDto, user),itemDtos);
            itemRequestsItemsDto.add(itemRequestItemDto);
        }
        return itemRequestsItemsDto;
    }

    @Override
    public List<ItemRequestItemsDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        usersRepository.findById(userId).orElseThrow(() ->
                new WrongUserException("Пользователя не существует id = " + userId));
        Pageable pageable;
       if (size == null || from == null) {
           pageable = Pageable.unpaged();
        } else if (size <= 0 || from < 0) {
           throw new BadRequestException("Ошибка параметров пагинации");
       } else {
           int page = from / size;
           pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"));
       }
       List<ItemRequestDto> itemRequests = itemRequestRepository.findAllByNotRequesterId(userId, pageable)
                .stream().map(ItemRequestMapper::toDto).collect(Collectors.toList());
       List<ItemRequestItemsDto> itemRequestItemsDtos = new ArrayList<>();
       for (ItemRequestDto itemRequestDto: itemRequests) {
            List<ItemOutDto> itemDtos = itemsRepository
                    .findAllByRequestId(itemRequestDto.getId())
                    .stream().map(ItemMapper::toItemOutDto).collect(Collectors.toList());
            User user = usersRepository.getOne(itemRequestDto.getRequesterId());
            ItemRequestItemsDto itemRequestItemDto =
                    ItemRequestMapper.toDtoItems(ItemRequestMapper.toRequest(itemRequestDto, user),itemDtos);
            itemRequestItemsDtos.add(itemRequestItemDto);
        }
        return itemRequestItemsDtos;
    }
}
