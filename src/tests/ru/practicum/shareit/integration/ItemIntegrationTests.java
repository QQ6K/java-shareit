package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingNodes;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.impl.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemIntegrationTests {

    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;

    @Test
    void create() {
        User user = new User(1L, "Test", "qwe@qwe.qwe");
        userService.createUser(UserMapper.toDto(user));
        User author = new User(2L, "Author", "asdasdqwe@qwe.qwe");
        Item item = new Item(123L, "Name", "Test", true, user, 123L);
        CommentDto comment1 = new CommentDto(1L, "etsdfdsf", author.getName(), LocalDateTime.now());
        CommentDto comment2 = new CommentDto(2L, "etsdfdsf", author.getName(), LocalDateTime.now());
        ItemDtoBookingNodes last = new ItemDtoBookingNodes(156L, 123L);
        ItemDtoBookingNodes next = new ItemDtoBookingNodes(183L, 547L);
        List<CommentDto> comments = List.of(comment1, comment2);
        ItemDto itemDto = ItemMapper.toItemBookingDto(item, comments, last, next);
        itemService.createItem(user.getId(), itemDto);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.description = :description", Item.class);
        Item itemOut = query
                .setParameter("description", item.getDescription())
                .getSingleResult();
        assertThat(itemOut.getDescription(), equalTo(itemDto.getDescription()));
        em.clear();
        em.close();
    }
}
