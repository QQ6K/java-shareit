package ru.practicum.shareit.user.exceptions;

public class UserCrudException extends RuntimeException{
    String param;
    String value;

    public UserCrudException(String message, String param, String value) {
        super(message);
        this.param = param;
        this.value = value;
    }

    public String getParam() {
        return param;
    }

    public String getValue() {
        return value;
    }
}
