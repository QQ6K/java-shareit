package ru.practicum.shareit.request;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
public class ItemRequestRepositoryTest {

    @Autowired
    private TestEntityManager tem;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    public void findAllByRequesterIdOrderByCreatedAscTest() {
        User user1 = new User(null, "user1", "qwe12@qwe.qwe");
        User user2 = new User(null, "user2", "qwe23@qwe.qwe");
        ItemRequest itemRequest1 = new ItemRequest(null, "description1", user1, LocalDateTime.now());
        ItemRequest itemRequest2 = new ItemRequest(null, "description2", user2, LocalDateTime.now());
        ItemRequest itemRequest3 = new ItemRequest(null, "description3", user2, LocalDateTime.now());

        tem.persist(user1);
        tem.persist(user2);
        tem.persist(itemRequest1);
        tem.persist(itemRequest2);
        tem.persist(itemRequest3);

        List<ItemRequest> result =
                itemRequestRepository.findAllByRequesterIdOrderByCreatedAsc(user2.getId());
        assertEquals(result.size(), 2);
        result = itemRequestRepository.findAllByRequesterIdOrderByCreatedAsc(user1.getId());
        assertEquals(result.size(), 1);
    }

    @Test
    public void findAllByNotRequesterId() {

    }
}