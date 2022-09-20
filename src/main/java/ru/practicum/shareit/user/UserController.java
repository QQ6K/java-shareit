package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.exceptions.UserCrudException;
import ru.practicum.shareit.user.interfaces.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }
    @PatchMapping("/{userId}")
    public Optional<User> patchUser(@PathVariable long userId, @Valid @RequestBody User user) throws UserCrudException {
        return userService.updateUser(userId,user);
    }
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) throws UserCrudException {
        userService.deleteUser(userId);
    }
    @GetMapping("/{userId}")
    public User findById(@PathVariable long userId) throws UserCrudException {
        return userService.readById(userId);
    }
    @GetMapping
    public Collection<User> findAll(){
        return userService.readAll();
    }
}
