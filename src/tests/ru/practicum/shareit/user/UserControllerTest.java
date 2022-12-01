package ru.practicum.shareit.user;

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
import org.springframework.web.util.NestedServletException;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration(classes = {UserController.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Test
    public void testDeleteUserNotFound() throws Exception {
        mvc.perform(delete("/user/123"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testDeleteUser2() throws Exception {
        doNothing().when(userService).deleteUser(Mockito.anyLong());
        MockHttpServletRequestBuilder requestBuilder = delete("/users/{userId}", 123L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void testPatchUser1() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("qweqwe@qwe.qwe");
        userDto.setName("name");
        when(userService.updateUser(1L, userDto))
                .thenReturn(Optional.of(UserMapper.fromDto(userDto)));
        mvc.perform(patch("/users/{userid}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1))
                .updateUser(1L, userDto);
    }


    @Test
    public void testReadAll() throws Exception {
        when(userService.readAll()).thenReturn(new ArrayList<>(List.of()));
        MockHttpServletRequestBuilder requestBuilder = get("/users");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testReadById() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("qwe@qwe.qwe");
        userDto.setName("Name");
        userDto.setId(123L);
        when(userService.readById(123L)).thenReturn(UserMapper.fromDto(userDto));
        MockHttpServletRequestBuilder requestBuilder = get("/users/{userId}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(status().isOk())
                .andExpect(content().json("{\"id\":123,\"name\":\"Name\",\"email\":\"qwe@qwe.qwe\"}"));
    }

    @Test(expected = CrudException.class)
    public void testReadByIdNotFound() throws Throwable {
        doThrow(CrudException.class).when(userService).readById(123L);
        try {
            mvc.perform(get("/users/123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (NestedServletException e) {throw e.getCause();}
    }

    @Test
    public void testSaveUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("qwe@qwe.qwe");
        userDto.setId(2L);
        userDto.setName("Name");
        String content = (mapper.writeValueAsString(userDto));
        when(userService.createUser(userDto))
                .thenReturn(Optional.of(UserMapper.fromDto(new UserDto(2L, "Name", "qwe@qwe.qwe"))));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(status().isOk())
                .andExpect(content().json("{\"id\":2,\"name\":\"Name\",\"email\":\"qwe@qwe.qwe\"}"));
    }


}