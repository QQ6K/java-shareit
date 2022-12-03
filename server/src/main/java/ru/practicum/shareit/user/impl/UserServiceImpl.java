package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.exceptions.EmailConflictException;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.interfaces.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {
    private final UsersRepository userRepository;

    @Override
    public User readById(Long userId) {
        log.info("Получение данных пользователя  id: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new CrudException("Пользователь не найден", "id", String.valueOf(userId)));
    }

    @Override
    @Transactional
    public Optional<User> createUser(UserDto userDto) {
        User user;
        try {
            user = userRepository.save(UserMapper.fromDto(userDto));
            log.info("Создание пользователя  id: {}", user.getId());
        } catch (DataIntegrityViolationException e) {
            throw new EmailConflictException("Пользователь с таким email уже существует", "email", userDto.getEmail());
        }
        log.info("Создание пользователя id = {}", userDto.getId());
        return userRepository.findById(user.getId());
    }

    @Override
    @Transactional
    public Optional<User> updateUser(long userId, UserDto userDto) {
        User updateUser = userRepository.findById(userId)
                .orElseThrow(() -> new CrudException("Пользователь не существует",
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
        userRepository.save(updateUser);
        log.info("Обновлены данные пользователя  id: {}", userId);
        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            log.info("Удален  пользователь  id: {}", userId);
        } else {
            throw new CrudException("Пользователь с таким id не существует", "id", String.valueOf(userId));
        }
    }

    @Override
    public Collection<User> readAll() {
        log.info("Получение всех пользователей");
        return userRepository.findAll();
    }

}
