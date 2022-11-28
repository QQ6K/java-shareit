package ru.practicum.shareit.user.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.practicum.shareit.user.UsersRepository;

import static org.junit.jupiter.api.Assertions.*;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.interfaces.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    private UserDto userDto = new UserDto(100L, "NameTest", "qweqwe@qwe.qwe");
    @Autowired
    private UserService userService;

    @MockBean
    private UsersRepository usersRepository;

    @Test
    public void readById() {
        userService.readById(123L);
    }

    @Test
    public void createUser() {
        User user = UserMapper.fromDto(userDto);
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(usersRepository.save(user)).thenReturn(user);
        Optional<User> userRes = userService.createUser(userDto);
        verify(usersRepository, times(1)).save(user);
        assertNotNull(userRes);
        assertEquals(Optional.of(user), userRes);
    }

    @Test
    public void updateUser() {
        userDto.setName("upName");
        when(usersRepository.findById(userDto.getId())).thenReturn(Optional.of(UserMapper.fromDto(userDto)));
        when(usersRepository.save(UserMapper.fromDto(userDto))).thenReturn(UserMapper.fromDto(userDto));
        Optional<User> userRes = userService.updateUser(100L, userDto);
        assertNotNull(userRes);
        assertEquals(Optional.of(UserMapper.fromDto(userDto)), userRes);
    }

    @Test
    public void deleteUser() {
        when(usersRepository.findById(userDto.getId())).thenReturn(Optional.of(UserMapper.fromDto(userDto)));
    }

    @Test
    public void readAll() {
    }
}