package ru.practicum.shareit.user.interfaces;


import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    User readById(long userId);

    User createUser(User user);

    Optional<User> updateUser(long userId, User user);

    void deleteUser(long userId);

    Collection<User> readAll();
}