package ru.practicum.shareit.exceptions;

public class WrongUserException extends CrudException {
    public WrongUserException(String message) {
        super(message);
    }
}
