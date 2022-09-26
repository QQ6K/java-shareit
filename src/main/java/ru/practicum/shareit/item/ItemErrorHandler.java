package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.UserNotExistException;
import ru.practicum.shareit.exceptions.UserNotOwnerException;

import java.util.Map;
import java.util.Objects;

@RestControllerAdvice("ru.practicum.shareit.item")
public class ItemErrorHandler {
    @ExceptionHandler(UserNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> userNotExistException(UserNotExistException e) {
        return Map.of("Ошибка", e.getMessage(), e.getParam(), e.getValue());
    }

    @ExceptionHandler(UserNotOwnerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> userNotOwnerException(UserNotOwnerException e) {
        return Map.of("Ошибка", e.getMessage(), e.getParam(), e.getValue());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> userHeaderException(MissingRequestHeaderException e) {
        return Map.of("Ошибка заголовка " + e.getHeaderName(), "Заголовок отсутствует или имеет неподходящий формат");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validExceptionItem(MethodArgumentNotValidException e) {
        return Map.of("Ошибка", Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

}
