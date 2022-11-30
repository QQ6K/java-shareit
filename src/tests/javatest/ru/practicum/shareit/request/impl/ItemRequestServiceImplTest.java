package javatest.ru.practicum.shareit.request.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.practicum.shareit.item.ItemsRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.request.interfaces.ItemRequestService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.interfaces.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ItemRequestServiceImpl.class)
public class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    @MockBean
    private ItemsRepository itemsRepository;

    ItemRequestDto itemRequestDto = new ItemRequestDto(200L, "Test", 100L, LocalDateTime.now());

    User userRequester = new User(100L, "John Doe", "qwe@qwe.qwe");

    ItemRequest itemRequest = new ItemRequest(200L, "Test", userRequester, LocalDateTime.now());

    ItemRequest itemRequest777 = new ItemRequest(
            777L,
            "777",
            new User(1L, "asd", "q@q.q"),
            LocalDateTime.now());
    ItemRequest itemRequest555 = new ItemRequest(
            555L,
            "555",
            new User(2L, "asd", "q@q.q"),
            LocalDateTime.now());


    @Test
    public void createTest() {
        LocalDateTime time = LocalDateTime.now();
        User user = new User(100L,"Test","qwe@qwe.qwe");
        ItemRequestDto itemRequestDtoNew = new ItemRequestDto(null,"Good",100L,time);
        ItemRequest itemRequestNew = new ItemRequest(null,"Good",user,time);
        doReturn(itemRequestNew).when(itemRequestRepository).save(itemRequestNew);
        doReturn(Optional.of(user)).when(usersRepository).findById(100L);
        ItemRequest itemRequestRes = itemRequestService.create(itemRequestDtoNew, 100L);
        assertNotNull(itemRequestRes);
        assertEquals(Optional.of(itemRequestRes).get().getDescription(), itemRequestRes.getDescription());
    }

    @Test
    public void findItemRequestByIdTest() {
        doReturn(Optional.of(itemRequest)).when(itemRequestRepository).findById(200L);
        doReturn(Optional.of(new User())).when(usersRepository).findById(100L);
        ItemRequestItemsDto itemRequestRes = itemRequestService.findItemRequestById(200L, 100L);
        assertNotNull(itemRequestRes);
        assertEquals("Test", itemRequestRes.getDescription());
    }

    @Test
    public void findAllItemRequestsByOwnerIdTest() {
        List<ItemRequest> itemRequests = List.of(itemRequest555, itemRequest777);
        User user777 = new User(100L, "777", "qwe777@qwe.qwe");
        itemRequest555.setRequester(user777);
        itemRequest777.setRequester(user777);
        doReturn(itemRequests).when(itemRequestRepository).findAllByRequesterIdOrderByCreatedAsc(100L);
        doReturn(Optional.of(new User())).when(usersRepository).findById(100L);
        doReturn(user777).when(usersRepository).getOne(100L);
        List<ItemRequestItemsDto> itemRequestRes = itemRequestService.findAllItemRequestsByOwnerId(100L);
        assertNotNull(itemRequestRes);
        assertEquals(itemRequests.size(), 2);
    }

    @Test
    public void getAllItemRequests() {
        Pageable pageable = PageRequest.of(1, 20, Sort.by(Sort.Direction.DESC, "created"));
        doReturn(Optional.of(new User(1L, "Test", "qweqwe@qwe.qwe")))
                .when(usersRepository).findById(Mockito.any());
        doReturn(new User(1L, "Test", "qweqwe@qwe.qwe")).when(usersRepository).getOne(Mockito.any());
        User user1 = new User(1L, "1", "qwe1@qwe.qwe");
        User user2 = new User(2L, "2", "qwe2@qwe.qwe");
        User user777 = new User(100L, "777", "qwe777@qwe.qwe");
        doReturn(user777).when(usersRepository).getOne(100L);
        ItemRequest itemRequest1 = new ItemRequest(1L, "Test1", user1, LocalDateTime.now());
        ItemRequest itemRequest2 = new ItemRequest(2L, "Test2", user2, LocalDateTime.now());
        Page<ItemRequest> itemRequests = new PageImpl<>(List.of(itemRequest1, itemRequest2));
        doReturn(itemRequests).when(itemRequestRepository).findAllByNotRequesterId(100L, pageable);
        List<ItemRequestItemsDto> itemRequestRes = itemRequestService.getAllItemRequests(100L, 20, 20);
        assertNotNull(itemRequestRes);
        assertEquals(itemRequests.getSize(), 2);
    }
}