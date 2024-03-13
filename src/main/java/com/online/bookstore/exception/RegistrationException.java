package com.online.bookstore.exception;

import lombok.Getter;

@Getter
public class RegistrationException extends RuntimeException {
    private final String message;

    public RegistrationException(final String message) {
        super(message);
        this.message = message;
    }

}
