package ru.practicum.shareit.exceptions;

public class NotExistException extends CrudException {
    public NotExistException(String message, String param, String value) {
        super(message, param, value);
    }
}
