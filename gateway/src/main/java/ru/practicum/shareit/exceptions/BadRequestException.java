package ru.practicum.shareit.exceptions;

public class BadRequestException extends CrudException {
    public BadRequestException(String message) {
        super(message);
    }
}
