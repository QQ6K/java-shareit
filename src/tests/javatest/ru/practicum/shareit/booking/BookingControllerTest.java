package javax.ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.web.util.NestedServletException;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoImport;
import ru.practicum.shareit.booking.interfaces.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {BookingController.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private BookingController bookingController;

    @MockBean
    private BookingService bookingService;

    @Test
    public void testPatchBooking() throws Exception {
        LocalDateTime localDateTimeStart = LocalDateTime.now();
        Booking booking = new Booking(
                153L,
                localDateTimeStart,
                localDateTimeStart,
                new Item(),
                new User(),
                BookingStatus.APPROVED);

        BookingDtoImport bookingDtoImport = new BookingDtoImport();
        bookingDtoImport.setEnd(localDateTimeStart);
        bookingDtoImport.setItemId(153L);
        bookingDtoImport.setStart(localDateTimeStart);

        doReturn(booking).when(bookingService).updateBooking(42L, 153L, true);
        MockHttpServletRequestBuilder patchResult = MockMvcRequestBuilders.patch("/bookings/{bookingId}", 153L);
        MockHttpServletRequestBuilder requestBuilder = patchResult.param("approved", String.valueOf(true))
                .header("X-Sharer-User-Id", 42L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .json("{\"item\":{},\"booker\":{},\"status\": \"APPROVED\",\"id\": 153}"));
    }

    @Test
    public void testReadAllOwner() throws Exception {
        when(bookingService.readAllOwner(42L, "WAITING", 0, 20)).thenReturn(new ArrayList<>(List.of()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/owner")
                .header("X-Sharer-User-Id", "42");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .json("[]"));
    }

    @Test
    public void testReadAllUser() throws Exception {
        when(bookingService.readAllUser(43L, "WAITING", 20, 20)).thenReturn(new ArrayList<>(List.of()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings")
                .header("X-Sharer-User-Id", "43");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .json("[]"));
    }


    @Test
    public void testReadById() throws Exception {
        LocalDateTime localDateTimeStart = LocalDateTime.now();

        Booking booking = new Booking(
                153L,
                localDateTimeStart,
                localDateTimeStart,
                new Item(),
                new User(),
                BookingStatus.APPROVED);

        BookingDtoImport bookingDtoImport = new BookingDtoImport();
        bookingDtoImport.setEnd(localDateTimeStart);
        bookingDtoImport.setItemId(153L);
        bookingDtoImport.setStart(localDateTimeStart);

        doReturn(booking).when(bookingService).createBooking(42L, bookingDtoImport);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/{bookingId}", 123L)
                .header("X-Sharer-User-Id", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test(expected = CrudException.class)
    public void testReadByIdNotFound() throws Throwable {
        doThrow(CrudException.class).when(bookingService).readById(123L, 42L);
        try {
            mvc.perform(get("/bookings/123")
                            .header("X-Sharer-User-Id", 42L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (NestedServletException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testSaveBooking() throws Exception {
        LocalDateTime localDateTimeStart = LocalDateTime.now();
        LocalDateTime localDateTimeEnd = localDateTimeStart.plusHours(1);
        BookingDtoImport bookingDtoImport = new BookingDtoImport();
        bookingDtoImport.setEnd(localDateTimeStart);
        bookingDtoImport.setItemId(153L);
        bookingDtoImport.setStart(localDateTimeEnd);
        String content = mapper.writeValueAsString(bookingDtoImport);
        Booking booking = new Booking(
                153L,
                bookingDtoImport.getStart(),
                bookingDtoImport.getEnd(),
                new Item(),
                new User(),
                BookingStatus.APPROVED);
        doReturn(booking).when(bookingService).createBooking(42L, bookingDtoImport);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/bookings")
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("{\"item\":{},\"booker\":{},\"status\": \"APPROVED\",\"id\": 153}"));
    }


}