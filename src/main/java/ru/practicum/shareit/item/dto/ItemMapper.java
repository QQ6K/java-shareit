package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public class ItemMapper {


    public static ItemDto toItemDto(Item item,
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

    public static Item fromDto(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner()
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

    public static OutputNewItemDto toOutputNewItemDto(Optional<Item> item) {
        OutputNewItemDto outputNewItemDto = new OutputNewItemDto();
        outputNewItemDto.setId(item.get().getId());
        outputNewItemDto.setName(item.get().getName());
        outputNewItemDto.setDescription(item.get().getDescription());
        outputNewItemDto.setAvailable(item.get().getAvailable());
        outputNewItemDto.setRequestId(null);
        return outputNewItemDto;
    }



}
