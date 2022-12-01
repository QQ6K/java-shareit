package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
class CommentsRepositoryTest {

    @Autowired
    TestEntityManager tem;

    @Autowired
    CommentsRepository commentsRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ItemsRepository itemsRepository;

    @Test
    void findAllByItemId() {
        User user1 = new User(null, "user1", "qwe1@qwe.qwe");
        User user2 = new User(null, "user2", "qwe2@qwe.qwe");
        Item item1 = new Item(null, "item1", "description1", true, user1, 123L);
        Item item2 = new Item(null, "item2", "description2", true, user2, 123154L);
        Item item3 = new Item(null, "item3", "description3", true, user2, 134L);
        Comment comment1 = new Comment(null, "Commentzczxc item1 user2", item1, user2, LocalDateTime.now());
        Comment comment2 = new Comment(null, "Comment asd item2 user1", item2, user1, LocalDateTime.now());
        Comment comment3 = new Comment(null, "Comment item3 user1", item3, user1, LocalDateTime.now());
        Comment comment4 = new Comment(null, "Comment item3 asdasd user1", item2, user2, LocalDateTime.now());
        tem.persist(user1);
        tem.persist(user2);
        tem.persist(item1);
        tem.persist(item2);
        tem.persist(item3);
        tem.persist(comment1);
        tem.persist(comment2);
        tem.persist(comment3);
        tem.persist(comment4);
        List<Comment> result1 =
                commentsRepository.findAllByItemId(item1.getId());
        int resSize1 = result1.size();
        assertEquals(1, resSize1);
        List<Comment> result2 = commentsRepository.findAllByItemId(item2.getId());
        int resSize2 = result2.size();
        assertEquals(2, resSize2);
    }
}