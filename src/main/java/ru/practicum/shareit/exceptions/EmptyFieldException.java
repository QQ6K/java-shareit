package ru.practicum.shareit.exceptions;

public class EmptyFieldException extends CrudException {
    public EmptyFieldException(String message) {
        super(message);
    }
}
