package ru.practicum.shareit.exceptions;

public class UserNotExistException extends CrudException{
    public UserNotExistException(String message, String param, String value) {
        super(message, param, value);
    }
}
