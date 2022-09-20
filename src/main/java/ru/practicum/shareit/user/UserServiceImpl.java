package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exceptions.UserCrudException;
import ru.practicum.shareit.user.interfaces.UserRepository;
import ru.practicum.shareit.user.interfaces.UserService;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public User readById(long userId) {
        return userRepository.readById(userId)
                .orElseThrow(() -> new UserCrudException("Пользователь не найден", "id", String.valueOf(userId)));
    }

    @Override
    public User createUser(User user) {
        if (userRepository.readByEmail(user.getEmail()).isEmpty() && Strings.isNotBlank(user.getEmail())) {
            return userRepository.save(user);
        } else {
            throw new UserCrudException("Пользователь с таким email уже существует", "email", user.getEmail());
        }
    }

    @Override
    public Optional<User> updateUser(long userId, User user) {
        User updatedUser = userRepository.readById(userId)
                .orElseThrow(() -> new UserCrudException("Пользователь с таким id не существует",
                        "id", String.valueOf(user.getId())));
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (userRepository.readByEmail(user.getEmail()).isEmpty()
                || userRepository.readByEmail(user.getEmail()).get().getEmail().equals(updatedUser.getEmail())) {
            updatedUser.setEmail(user.getEmail());
        } else {
            throw new UserCrudException("Пользователь с таким email уже существует", "email", user.getEmail());
        }
        userRepository.save(updatedUser);
        return userRepository.readById(userId);
    }

    @Override
    public void deleteUser(long userId) {
        if (userRepository.readById(userId).isPresent()) {
            userRepository.deleteById(userId);
            log.info("Удален  пользователь  id: {}", userId);

        } else {
            throw new UserCrudException("Пользователь с таким id не существует", "id", String.valueOf(userId));
        }
    }

    @Override
    public Collection<User> readAll() {
        return userRepository.readAll();
    }

}
