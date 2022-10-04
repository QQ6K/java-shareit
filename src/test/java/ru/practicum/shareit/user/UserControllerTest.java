package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import resources.EntitiesForTests;
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;
    @Autowired
    MockMvc mvc;

    @Order(1)
    @Test
    void addUserTest() throws Exception {
        UserDto testDtoUser1 = EntitiesForTests.getTestUserDto1();
        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testDtoUser1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Паша"))
                .andExpect(jsonPath("$.email").value("test1@test.test"));
    }

    @Order(2)
    @Test
    void getUserTest() throws Exception {
        UserDto testDtoUser2 = EntitiesForTests.getTestUserDto2();
        mvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(testDtoUser2))
                .contentType(MediaType.APPLICATION_JSON));
        mvc.perform(get("/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Миша"))
                .andExpect(jsonPath("$.email").value("doornail@test.test"));
    }

    @Order(3)
    @Test
    void deleteUserTest() throws Exception {
        mvc.perform(delete("/users/1")).andExpect(status().isOk());
    }

    @Order(4)
    @Test
    void getAllUserTest() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").value(2));
    }

    @Order(5)
    @Test
    void patchUserTest() throws Exception {
        UserDto testDtoUser2 = EntitiesForTests.getTestUserDto2();
        testDtoUser2.setEmail("1@test.test");
        mvc.perform(patch("/users/2")
                .content(objectMapper.writeValueAsString(testDtoUser2))
                .contentType(MediaType.APPLICATION_JSON));
        mvc.perform(get("/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Миша"))
                .andExpect(jsonPath("$.email").value("1@test.test"));
    }

}
