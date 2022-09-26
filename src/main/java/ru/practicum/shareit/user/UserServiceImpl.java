package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailConflictException;
import ru.practicum.shareit.exceptions.EmptyFieldException;
import ru.practicum.shareit.exceptions.NotExistException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
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
    public User readById(Long userId) {
        log.info("Создание пользователя  id: {}", userId);
        return userRepository.readById(userId)
                .orElseThrow(() -> new NotExistException("Пользователь не найден", "id", String.valueOf(userId)));
    }

    @Override
    public Optional<User> createUser(UserDto userDto) {
        User user;
        if (Strings.isNotBlank(userDto.getEmail())) {
            if (userRepository.readByEmail(userDto.getEmail()).isEmpty()) {
                log.info("Создание пользователя  id: {}", userDto.getId());
                user = userRepository.create(UserMapper.fromDto(userDto));
            } else {
                throw new EmailConflictException("Пользователь с таким email уже существует", "email", userDto.getEmail());
            }
        } else throw new EmptyFieldException("Необходимо заполнить email");
        return userRepository.readById(user.getId());
    }

    @Override
    public Optional<User> updateUser(long userId, UserDto userDto) {
        User updateUser = userRepository.readById(userId)
                .orElseThrow(() -> new NotExistException("Пользователь не существует",
                        "id", String.valueOf(userDto.getId())));
        if (userDto.getName() != null) {
            updateUser.setName(userDto.getName());
        }
        if (userRepository.readByEmail(userDto.getEmail()).isEmpty()) {
            if (userDto.getEmail() != null) {
                updateUser.setEmail(userDto.getEmail());
            }
        } else {
            throw new EmailConflictException("Пользователь с таким email уже существует", "email", userDto.getEmail());
        }
        userRepository.update(updateUser);
        log.info("Обновлены данные пользователя  id: {}", userId);
        return userRepository.readById(userId);
    }

    @Override
    public void deleteUser(long userId) {
        if (userRepository.readById(userId).isPresent()) {
            userRepository.deleteUser(userId);
            log.info("Удален  пользователь  id: {}", userId);
        } else {
            throw new NotExistException("Пользователь с таким id не существует", "id", String.valueOf(userId));
        }
    }

    @Override
    public Collection<User> readAll() {
        log.info("Получение всех пользователей");
        return userRepository.readAll();
    }

}
