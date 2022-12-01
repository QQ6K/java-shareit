package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
class UsersRepositoryTest {

    @Autowired
    TestEntityManager tem;

    @Autowired
    UsersRepository usersRepository;

    @Test
    public void readByEmail() {
        User user1 = new User(null, "user1", "qwe1@qwe.qwe");
        User user2 = new User(null, "user2", "qwe2@qwe.qwe");
        tem.persist(user1);
        tem.persist(user2);
        Optional<User> user =
                usersRepository.readByEmail("qwe2@qwe.qwe");
        assertEquals(user.get().getEmail(), "qwe2@qwe.qwe");
    }


}