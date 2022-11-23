package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public class ItemMapper {


    public static ItemDto toItemBookingDto(Item item,
                                    List<CommentDto> commentsDto,
                                    ItemDtoBookingNodes lastBooking,
                                    ItemDtoBookingNodes nextBooking) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setComments(commentsDto);
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        return itemDto;
    }

    public static ItemOutDto toItemOutDto(Item item) {
        ItemOutDto itemOutDto = new ItemOutDto();
        itemOutDto.setId(item.getId());
        itemOutDto.setName(item.getName());
        itemOutDto.setDescription(item.getDescription());
        itemOutDto.setAvailable(item.getAvailable());
        itemOutDto.setOwner(item.getOwner());
        itemOutDto.setRequestId(item.getRequestId());
        return itemOutDto;
    }

    public static Item fromDto(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner(),
                itemDto.getRequestId()
                );
    }

    public static ItemDtoShort toItemDtoShort(Item item,
                                    ItemDtoBookingNodes lastBooking,
                                    ItemDtoBookingNodes nextBooking) {
        ItemDtoShort itemDtoShort = new ItemDtoShort();
        itemDtoShort.setId(item.getId());
        itemDtoShort.setName(item.getName());
        itemDtoShort.setDescription(item.getDescription());
        itemDtoShort.setAvailable(item.getAvailable());
        itemDtoShort.setLastBooking(lastBooking);
        itemDtoShort.setNextBooking(nextBooking);
        return itemDtoShort;
    }

}
