package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.EmailConflictException;
import ru.practicum.shareit.exceptions.ErrorResponse;
import ru.practicum.shareit.exceptions.UserValidException;

import java.util.Map;
import java.util.Objects;

@RestControllerAdvice("ru.practicum.shareit.user")
@Slf4j
public class UserErrorHandler {
    @ExceptionHandler(EmailConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflictException(EmailConflictException e) {
        log.error(e.getMessage());
        return Map.of("Конфликт", e.getMessage(), e.getParam(), e.getValue());
    }

    @ExceptionHandler(UserValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> emptyException(UserValidException e) {
        log.error(e.getMessage());
        return Map.of("Ошибка", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validExceptionUser(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return Map.of("Ошибка", Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Throwable e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

}
