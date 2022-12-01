package ru.practicum.shareit.user.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.practicum.shareit.user.UsersRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.interfaces.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserServiceImpl.class)
public class UserServiceImplTest {

    final private UserDto userDto = new UserDto(100L, "NameTest", "qweqwe@qwe.qwe");

    @Autowired
    private UserService userService;

    @MockBean
    private UsersRepository usersRepository;

    @Test
    public void readByIdTestTest() {
        User user = UserMapper.fromDto(userDto);
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User userRes = userService.readById(100L);
        assertNotNull(userRes);
        assertEquals(Optional.of(user), Optional.of(userRes));
    }

    @Test
    public void createUserTest() {
        User user = UserMapper.fromDto(userDto);
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(usersRepository.save(user)).thenReturn(user);
        Optional<User> userRes = userService.createUser(userDto);
        verify(usersRepository, times(1)).save(user);
        assertNotNull(userRes);
        assertEquals(Optional.of(user), userRes);
    }

    @Test
    public void updateUserTest() {
        userDto.setName("upName");
        when(usersRepository.findById(userDto.getId())).thenReturn(Optional.of(UserMapper.fromDto(userDto)));
        when(usersRepository.save(UserMapper.fromDto(userDto))).thenReturn(UserMapper.fromDto(userDto));
        Optional<User> userRes = userService.updateUser(100L, userDto);
        assertNotNull(userRes);
        assertEquals(Optional.of(UserMapper.fromDto(userDto)), userRes);
    }

    @Test
    public void deleteUserTest() {
        doNothing().when(usersRepository).deleteById(100L);
        when(usersRepository.findById(100L)).thenReturn(Optional.of(new User()));
        userService.deleteUser(100L);
        verify(usersRepository, times(1)).deleteById(100L);
    }

    @Test
    public void readAllTest() {
        when(usersRepository.findAll()).thenReturn(new ArrayList<>());
        Collection<User> userList = userService.readAll();
        assertNotNull(userList);
        assertEquals(new ArrayList<>(), userList);
    }
}