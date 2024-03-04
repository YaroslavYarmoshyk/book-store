package com.online.bookstore.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final String message;

    public EntityNotFoundException(final String message) {
        super(message);
        this.message = message;
    }
}
