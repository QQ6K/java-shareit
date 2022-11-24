package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.interfaces.UserService;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @Order(1)
    void saveUser_shouldReturnUser() throws Exception {
        String jsonContent = "{\"name\":\"Name\", \"email\":\"test@test.test\"}";

        this.mockMvc
                .perform(post("/users")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void patchUser_shouldUpdateUser() throws Exception {
        String jsonContent = "{\"name\":\"Name1\", \"email\":\"test@test.test\"}";

        this.mockMvc
                .perform(patch("/users/1")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void readById() throws Exception {
        this.mockMvc.perform(get("/users/1")).andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void deleteUser() throws Exception {
        this.mockMvc
                .perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void readAll() throws Exception {
        when(userService.readAll()).thenReturn(Collections.emptyList());

        this.mockMvc
                .perform(get("/users"))
                .andExpect(status().isOk());
    }
}