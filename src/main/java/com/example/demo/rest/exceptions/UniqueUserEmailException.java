package com.example.demo.rest.exceptions;

public class UniqueUserEmailException extends RuntimeException {
    public UniqueUserEmailException(String message) {
        super(message);
    }
}
