package ru.practicum.shareit.user.interfaces;


import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    User readById(Long userId);

    Optional<User> createUser(UserDto userDto);

    Optional<User> updateUser(long userId, UserDto userDto);

    void deleteUser(long userId);

    Collection<User> readAll();
}