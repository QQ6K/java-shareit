package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.UserNotExistException;
import ru.practicum.shareit.exceptions.UserNotOwnerException;

import java.util.Map;

@RestControllerAdvice("ru.practicum.shareit.item")
@ControllerAdvice
public class ItemErrorHandler {
    @ExceptionHandler(UserNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> userNotExistException(UserNotExistException e) {
        return Map.of("Ошибка",e.getMessage(), e.getParam(), e.getValue());
    }

    @ExceptionHandler(UserNotOwnerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> userNotOwnerException(UserNotOwnerException e) {
        return Map.of("Ошибка",e.getMessage(), e.getParam(), e.getValue());
    }

}
