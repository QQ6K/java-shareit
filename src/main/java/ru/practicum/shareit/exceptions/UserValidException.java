package ru.practicum.shareit.exceptions;

public class UserValidException extends CrudException {
    public UserValidException(String message, String param, String value) {
        super(message, param, value);
    }
}
