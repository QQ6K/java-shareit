package ru.practicum.shareit.item.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.practicum.shareit.item.ItemsRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingNodes;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)

@SpringBootTest
public class ItemServiceImplTest {

    ItemDto itemDto = new ItemDto(3561L, "Thing", "Very good", true, new User(100L, "T", "T"), 123L,
            Collections.emptyList(), new ItemDtoBookingNodes(), new ItemDtoBookingNodes());

    User userItem = new User(100L, "T", "T");

    Item item = new Item(3561L, "Thing", "Very good", true, userItem, 123L);

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemsRepository itemsRepository;

    @MockBean
    private UsersRepository usersRepository;

    @Test
    public void readById() {
        doReturn(Optional.of(item)).when(itemsRepository).findById(3561L);
        doReturn(Optional.of(new User())).when(usersRepository).findById(100L);
        ItemDto itemRes = itemService.readById(3561L, 100L);
        assertNotNull(itemRes);
        assertEquals(itemDto.getId(), itemRes.getId());
    }

    @Test
    public void readAll() {
        doReturn(Optional.of(new User())).when(usersRepository).findById(100L);
        when(itemsRepository.findAll()).thenReturn(new ArrayList<>());
        Collection<ItemDtoShort> itemList = itemService.readAll(100L);
        assertNotNull(itemList);
        assertEquals(new ArrayList<>(), itemList);
    }

    @Test
    public void createItemTest() {
        when(itemsRepository.findById(3561L)).thenReturn(Optional.of(item));
        when(usersRepository.findById(100L)).thenReturn(Optional.of(new User()));
        ItemOutDto itemRes = itemService.createItem(100L, itemDto);
        assertNotNull(itemRes);
        assertEquals("Very good", itemRes.getDescription());
    }

    @Test
    public void updateItem() {
        itemDto.setDescription("New");
        when(itemsRepository.findById(3561L)).thenReturn(Optional.of(item));
        when(usersRepository.findById(100L)).thenReturn(Optional.of(userItem));

        Optional<Item> itemRes = itemService.updateItem(100L, 3561L, itemDto);
        assertNotNull(itemRes);
        assertEquals(3561L, itemRes.get().getId());
    }

    @Test
    public void deleteItem() {
        when(itemsRepository.findById(100L)).thenReturn(Optional.of(new Item()));
        doNothing().when(usersRepository).deleteById(100L);
        itemService.deleteItem(100L);
        verify(itemsRepository, times(1)).deleteById(100L);
    }

    @Test
    public void searchText() {
    }

    @Test
    public void addComment() {
    }
}