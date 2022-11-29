package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingNodes;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration(classes = {ItemController.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private ItemController itemController;

    @MockBean
    private ItemService itemService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS"
    );

    @Test
    public void testReadById1() throws Exception {
        when(itemService.readById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(new ItemDto());
        MockHttpServletRequestBuilder requestBuilder = get("/items/{itemId}", 1L)
                .header("X-Sharer-User-Id", 1L);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(
                                "{\"id\":null,\"name\":null,\"description\":null,\"available\":null,\"owner\":null,\"requestId\":null,\"comments\""
                                        + ":null,\"lastBooking\":null,\"nextBooking\":null}"));
    }

    @Test
    public void testReadById2() throws Exception {
        when(itemService.readAll(Mockito.anyLong())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = get("/items/{itemId}", 1L)
                .header("X-Sharer-User-Id", 1L);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void testReadById3() throws Exception {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        CommentDto commentDto1 = new CommentDto();
        commentDto1.setAuthorName("JohnDoe");
        LocalDateTime localDateTime = LocalDateTime.now();
        String localDateTimeString = localDateTime.toString();
        localDateTimeString = localDateTimeString.substring(0, localDateTimeString.length() - 2);
        commentDto1.setCreated(LocalDateTime.parse(localDateTimeString));
        commentDto1.setId(1);
        commentDto1.setText("Text");

        CommentDto commentDto2 = new CommentDto();
        commentDto2.setAuthorName("Jim Doe");
        localDateTime = LocalDateTime.now();
        localDateTimeString = localDateTime.toString();
        localDateTimeString = localDateTimeString.substring(0, localDateTimeString.length() - 2);
        commentDto2.setCreated(LocalDateTime.parse(localDateTimeString, formatter));
        commentDto2.setId(1);
        commentDto2.setText("Text");
        List<CommentDto> commentDtos = List.of(commentDto1, commentDto2);

        ItemDtoBookingNodes itemDtoBookingNodes1 = new ItemDtoBookingNodes();
        itemDtoBookingNodes1.setId(1L);
        itemDtoBookingNodes1.setBookerId(25L);

        ItemDtoBookingNodes itemDtoBookingNodes2 = new ItemDtoBookingNodes();
        itemDtoBookingNodes1.setId(1L);
        itemDtoBookingNodes1.setBookerId(35L);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Name");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);
        itemDto.setOwner(user);
        itemDto.setRequestId(1L);
        itemDto.setComments(commentDtos);
        itemDto.setLastBooking(itemDtoBookingNodes1);
        itemDto.setNextBooking(itemDtoBookingNodes2);

        when(itemService.readById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemDto);
        mvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));

    }

    @Test
    public void testReadAll() throws Exception {
        when(itemService.readAll(Mockito.anyLong())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = get("/items")
                .header("X-Sharer-User-Id", 123L);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testAddComment1() throws Exception {
        CommentDto commentDto1 = new CommentDto();
        commentDto1.setAuthorName("JohnDoe");
        LocalDateTime localDateTime = LocalDateTime.now();
        String localDateTimeString = localDateTime.toString();
        localDateTimeString = (char) 34 + localDateTimeString.substring(0, localDateTimeString.length() - 2) + (char) 34;
        commentDto1.setCreated(localDateTime);
        commentDto1.setId(1);
        commentDto1.setText("Text");
        when(itemService.addComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(commentDto1);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto1))
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"text\":\"Text\",\"authorName\":\"JohnDoe\",\"created\":" +
                        localDateTimeString + "}"));
    }


    @Test
    public void testAddComment2() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthorName("JaneDoe");
        LocalDateTime localDateTime = LocalDateTime.now();
        commentDto.setCreated(localDateTime);
        commentDto.setId(1);
        commentDto.setText("Text");
        String content = mapper.writeValueAsString(commentDto);
        MockHttpServletRequestBuilder requestBuilder = post("/items/{itemId}/comment", 123L)
                .header("X-Sharer-User-Id", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder);
        actualPerformResult
                .andExpect(status().is(200))
        ;
    }


    @Test
    public void testDeleteItem() throws Exception {
        doNothing().when(itemService).deleteItem(Mockito.anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/items/{itemId}", 123L);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void testFindByText() throws Exception {
        when(itemService.searchText(Mockito.anyString())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = get("/items/search").param("text", "test");
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("[]"));
    }

    @Test
    public void testPatchItem() throws Exception {
        User user = new User();
        user.setEmail("qwe@qwe.qwe");
        user.setId(123L);
        user.setName("Name");

        Item item = new Item();
        item.setAvailable(true);
        item.setDescription("Description");
        item.setId(123L);
        item.setName("Name");
        item.setOwner(user);
        item.setRequestId(123L);
        Optional<Item> ofResult = Optional.of(item);
        when(itemService.updateItem(Mockito.anyLong(), Mockito.anyLong(), any())).thenReturn(ofResult);

        User user1 = new User();
        user1.setEmail("qweqwe@qwe.qwe");
        user1.setId(123L);
        user1.setName("Name");

        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(true);
        itemDto.setComments(new ArrayList<>());
        itemDto.setDescription("Description");
        itemDto.setId(123L);
        itemDto.setLastBooking(new ItemDtoBookingNodes());
        itemDto.setName("Name");
        itemDto.setNextBooking(new ItemDtoBookingNodes());
        itemDto.setOwner(user1);
        itemDto.setRequestId(123L);
        String content = mapper.writeValueAsString(itemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/items/{itemId}", 123L)
                .header("X-Sharer-User-Id", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content()
                        .string(
                                "{\"id\":123,\"name\":\"Name\",\"description\":" +
                                        "\"Description\",\"available\":true"
                                        + ",\"owner\":{\"id\":123,\"name\":\"Name\",\"email\":\"qwe@qwe.qwe\"}," +
                                        "\"requestId\":123}"));
    }


    @Test
    public void testSaveItem() throws Exception {
        User user = new User();
        user.setEmail("qwe@qwe.qwe");
        user.setId(123L);
        user.setName("Name");
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(true);
        itemDto.setComments(new ArrayList<>());
        itemDto.setDescription("Test");
        itemDto.setId(123L);
        itemDto.setLastBooking(new ItemDtoBookingNodes());
        itemDto.setName("Name");
        itemDto.setNextBooking(new ItemDtoBookingNodes());
        itemDto.setOwner(user);
        itemDto.setRequestId(12L);
        ItemOutDto itemOutDto = new ItemOutDto(123L, "Name", "Test", true, user, 12L);
        doReturn(itemOutDto).when(itemService).createItem(42L, itemDto);
        String content = (mapper.writeValueAsString(itemDto));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/items")
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":123,\"name\":\"Name\"," +
                                "\"description\":\"Test\",\"available\":true,\"owner\":" +
                                "{\"id\":123,\"name\":\"Name\",\"email\":\"qwe@qwe.qwe\"},\"requestId\":12}"));
    }
}