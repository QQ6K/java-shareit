package ru.practicum.shareit.exceptions;

public class EmailConflictException extends CrudException {
    public EmailConflictException(String message, String param, String value) {
        super(message, param, value);
    }
}
