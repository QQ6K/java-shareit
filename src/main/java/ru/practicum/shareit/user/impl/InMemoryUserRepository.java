package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.interfaces.UserRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> userDataRep = new HashMap<>();
    private long id = 1;

    @Override
    public Optional<User> readById(long userId) {
        return userDataRep.keySet().stream().filter(i -> i == userId)
                .map(userDataRep::get)
                .findAny();
    }

    @Override
    public Optional<User> readByEmail(String email) {
        return userDataRep
                .values()
                .stream()
                .filter(u -> u.getEmail().equals(email)).findAny();
    }

    @Override
    public User save(User user) {
        user.setId(id);
        id++;
        userDataRep.put(user.getId(), user);
        return userDataRep.get(user.getId());
    }

    @Override
    public void deleteById(long userId) {
        userDataRep.remove(userId);
    }

    @Override
    public Collection<User> readAll() {
        return null;
    }
}
