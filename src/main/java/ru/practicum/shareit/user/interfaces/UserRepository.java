package ru.practicum.shareit.user.interfaces;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Optional<User> readById(long userId);

    Optional<User> readByEmail(String email);

    User create(User user);

    User update(User user);

    void deleteUser(Long userId);

    Collection<User> readAll();

}