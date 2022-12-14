package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ErrorHandlerController {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> illegalArgumentHandle(IllegalArgumentException e) {
        log.error(e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> constraintViolationHandle(ConstraintViolationException e) {
        log.error(e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({WrongUserException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> wrongException(WrongUserException e) {
        log.error(e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({StateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> stateException(StateException e) {
        log.error(e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> notAvailableException(BadRequestException e) {
        log.error(e.getMessage());
        return Map.of("Ошибка:", e.getMessage());
    }

    @ExceptionHandler({CrudException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> userValidException(CrudException e) {
        log.error(e.getMessage());
        return Map.of("Ошибка", e.getMessage(), e.getParam(), e.getValue());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> userHeaderException(MissingRequestHeaderException e) {
        log.error(e.getMessage());
        return Map.of("Ошибка заголовка " + e.getHeaderName(),
                "Заголовок отсутствует или имеет неподходящий формат");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return Map.of("Ошибка", Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(EmailConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflictException(EmailConflictException e) {
        log.error(e.getMessage());
        return Map.of("Конфликт", e.getMessage(), e.getParam(), e.getValue());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Throwable e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
