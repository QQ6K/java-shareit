package ru.practicum.shareit.exceptions;

public class UserNotOwnerException extends CrudException {
    public UserNotOwnerException(String message, String param, String value) {
        super(message, param, value);
    }
}
