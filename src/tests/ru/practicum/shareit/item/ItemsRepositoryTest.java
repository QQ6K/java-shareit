package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
class ItemsRepositoryTest {

    @Autowired
    TestEntityManager tem;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ItemsRepository itemsRepository;

    @Test
    public void findAllByRequestId() {
        User user1 = new User(null, "user1", "qwe1@qwe.qwe");
        User user2 = new User(null, "user2", "qwe2@qwe.qwe");
        Item item1 = new Item(null, "item1", "description1", true, user1, 123L);
        Item item2 = new Item(null, "item2", "description2", true, user2, 123154L);
        Item item3 = new Item(null, "item3", "description3", true, user2, 134L);
        Item item4 = new Item(null, "item4", "description4", true, user1, 2324L);
        Item item5 = new Item(null, "item5", "description5", true, user2, 123154L);

        tem.persist(user1);
        tem.persist(user2);
        tem.persist(item1);
        tem.persist(item2);
        tem.persist(item3);
        tem.persist(item4);
        tem.persist(item5);

        List<Item> result =
                itemsRepository.findAllByRequestId(123154L);
        assertEquals(result.size(), 2);
    }


}