package com.example.demo.rest.exceptions;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(String message) {
        super(message);
    }
}
