package ru.practicum.shareit.user.interfaces;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Optional<User> readById(long userId);

    Optional<User> readByEmail(String email);

    User save(User user);

    void deleteById(long userId);

    Collection<User> readAll();

}