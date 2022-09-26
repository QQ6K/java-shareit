package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.CrudException;
import ru.practicum.shareit.exceptions.EmailConflictException;
import ru.practicum.shareit.exceptions.EmptyFieldException;
import ru.practicum.shareit.exceptions.NotExistException;

import java.util.Map;

@RestControllerAdvice("ru.practicum.shareit.user")
@ControllerAdvice
public class UserErrorHandler {
    @ExceptionHandler(NotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> notExistException(NotExistException e) {
        return Map.of("Данные", e.getMessage(), e.getParam(), e.getValue());
    }

    @ExceptionHandler(EmailConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflictException(EmailConflictException e) {
        return Map.of("Конфликт", e.getMessage(), e.getParam(), e.getValue());
    }

    @ExceptionHandler(EmptyFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> emptyException(EmptyFieldException e) {
        return Map.of("Ошибка", e.getMessage());
    }
}
