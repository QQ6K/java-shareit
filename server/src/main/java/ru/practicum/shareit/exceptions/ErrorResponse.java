package ru.practicum.shareit.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResponse {

    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }


}