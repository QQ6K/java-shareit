package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.impl.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestIntegrationTest {

    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final ItemRequestServiceImpl requestService;


    @Test
    void create() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(653L, "Description", 155L, LocalDateTime.now());
        UserDto userDto = new UserDto(1L, "Name", "qweqwe@qweqwe.qweqwe");
        userService.createUser(userDto);
        requestService.create(itemRequestDto, userDto.getId());
        TypedQuery<ItemRequest> query =
                em.createQuery("Select ir from ItemRequest ir where ir.description = :id", ItemRequest.class);
        ItemRequest itemRequestOut = query
                .setParameter("id", itemRequestDto.getDescription())
                .getSingleResult();
        assertThat(itemRequestOut.getDescription(), equalTo(itemRequestDto.getDescription()));
    }
}
