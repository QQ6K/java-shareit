package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.interfaces.UserService;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public Optional<User> saveUser(@Valid @RequestBody UserDto userdto) {
        log.info("Запрос 'POST /users'");
        return userService.createUser(userdto);
    }

    @PatchMapping("/{userId}")
    public Optional<User> patchUser(@Valid @PathVariable long userId, @RequestBody UserDto userdto) {
        log.info("Запрос 'PATCH /users/{}'", userId);
        return userService.updateUser(userId, userdto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) throws CrudException {
        log.info("Запрос 'DELETE /users/{}'", userId);
        userService.deleteUser(userId);
    }

    @GetMapping("/{userId}")
    public User readById(@PathVariable long userId) throws CrudException {
        log.info("Запрос 'GET /users/{}'", userId);
        return userService.readById(userId);
    }

    @GetMapping
    public Collection<User> readAll() {
        log.info("Запрос 'GET /users'");
        return userService.readAll();
    }
}
