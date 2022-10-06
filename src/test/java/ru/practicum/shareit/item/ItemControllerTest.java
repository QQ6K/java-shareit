package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UsersRepository userRepository;

    @Autowired
    ItemsRepository itemRepository;

    @Autowired
    MockMvc mvc;

    @Order(1)
    @Test
    void addItemTest() throws Exception {
        ItemDto testItemDto1 = EntitiesForTests.getTestItem1();
        ItemDto testItemDto2 = EntitiesForTests.getTestItem2();
        ItemDto testItemDto3 = EntitiesForTests.getTestItem3();
        UserDto testDtoUser3 = EntitiesForTests.getTestUserDto3();

        mvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(testDtoUser3))
                .contentType(MediaType.APPLICATION_JSON));

        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(testItemDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гвоздь"))
                .andExpect(jsonPath("$.description").value("Очень хорошие гвозди"))
                .andExpect(jsonPath("$.available").value("true"))
                .andExpect(jsonPath("$.owner").value("3"))
                .andExpect(jsonPath("$.request").value("ссылка.гвоздь.1"));


        mvc.perform(post("/items")
                .content(objectMapper.writeValueAsString(testItemDto2))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 2));

        mvc.perform(post("/items")
                .content(objectMapper.writeValueAsString(testItemDto3))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 3));
    }

    @Order(2)
    @Test
    void getItemTest() throws Exception {
        mvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гвоздь"))
                .andExpect(jsonPath("$.description").value("Очень хорошие гвозди"))
                .andExpect(jsonPath("$.available").value("true"))
                .andExpect(jsonPath("$.owner").value("3"))
                .andExpect(jsonPath("$.request").value("ссылка.гвоздь.1"));
    }

    @Order(3)
    @Test
    void patchItemTest() throws Exception {
        ItemDto testItemDto2 = EntitiesForTests.getTestItem2();
        testItemDto2.setName("Цветные цветы");
        mvc.perform(patch("/items/2")
                .content(objectMapper.writeValueAsString(testItemDto2))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 2));
        mvc.perform(get("/items/2").header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Цветные цветы"))
                .andExpect(jsonPath("$.description").value("Цветы для подоконника"))
                .andExpect(jsonPath("$.available").value("true"))
                .andExpect(jsonPath("$.owner").value("2"))
                .andExpect(jsonPath("$.request").value("ссылка.цвето.ком"));
    }

    @Order(4)
    @Test
    void deleteItemTest() throws Exception {
        mvc.perform(delete("/items/1")).andExpect(status().isOk());
        mvc.perform(delete("/items/2")).andExpect(status().isOk());
    }

    @Order(5)
    @Test
    void getAllUserTest() throws Exception {
        mvc.perform(get("/items").header("X-Sharer-User-Id", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").value(3));
    }


}
