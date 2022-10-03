package ru.practicum.shareit.exceptions;

public class EmptyUserValidException extends CrudException {
    public EmptyUserValidException(String message, String param, String value) {
        super(message, param, value);
    }
}
