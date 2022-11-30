package javax.ru.practicum.shareit.request;

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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.impl.ItemRequestServiceImpl;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ItemRequestController.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    private ItemRequestController itemRequestController;

    @MockBean
    private ItemRequestServiceImpl itemRequestServiceImpl;

    @Autowired
    MockMvc mvc;

   @Test
    public void testCreateNewRequest() throws Exception {
        when(itemRequestServiceImpl.findAllItemRequestsByOwnerId(Mockito.anyLong()))
                .thenReturn(new ArrayList<>());

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Description");
        itemRequestDto.setRequesterId(1L);
        String content = (new ObjectMapper()).writeValueAsString(itemRequestDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/requests")
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testFindItemRequestById() throws Exception {
        when(itemRequestServiceImpl.findItemRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(new ItemRequestItemsDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/requests/{requestId}", 12L)
                .header("X-Sharer-User-Id", 2L);
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                .string("{\"id\":null,\"description\":null,\"created\":null,\"items\":null,\"requester_id\":null}"));
    }

    @Test
    public void testFindRequestById2() throws Exception {
        when(itemRequestServiceImpl.findAllItemRequestsByOwnerId(Mockito.anyLong()))
                .thenReturn(new ArrayList<>());
        when(itemRequestServiceImpl.findItemRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(new ItemRequestItemsDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requests/{requestId}", "", "Uri Vars")
                .header("X-Sharer-User-Id", 1L);
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testGetAllRequests() throws Exception {
        when(itemRequestServiceImpl.getAllItemRequests(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requests/all")
                .header("X-Sharer-User-Id", 1L);
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testGetAllRequestsByOwnerId() throws Exception {
        when(itemRequestServiceImpl.findAllItemRequestsByOwnerId(Mockito.anyLong())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requests")
                .header("X-Sharer-User-Id", 1L);
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}