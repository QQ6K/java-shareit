package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.interfaces.UserRepository;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> userRepository = new HashMap<>();
    private long id = 1;

    @Override
    public Optional<User> readById(long userId) {
        return userRepository.keySet().stream().filter(i -> i == userId)
                .map(userRepository::get)
                .findAny();
    }

    @Override
    public Optional<User> readByEmail(String email) {
        return userRepository
                .values()
                .stream()
                .filter(u -> u.getEmail().equals(email)).findAny();
    }

    @Override
    public User create(User user) {
        user.setId(id);
        id++;
        userRepository.put(user.getId(), user);
        return userRepository.get(user.getId());
    }

    @Override
    public User update(User user) {
        userRepository.put(user.getId(), user);
        return userRepository.get(user.getId());
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.remove(userId);
    }

    @Override
    public Collection<User> readAll() {
        return new ArrayList<>(userRepository.values());
    }
}
