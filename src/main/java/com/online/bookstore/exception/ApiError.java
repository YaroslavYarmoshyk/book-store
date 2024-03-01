package com.online.bookstore.exception;

import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.http.HttpStatus;

public record ApiError(
        LocalDateTime timestamp,
        HttpStatus status,
        int code,
        Collection<String> errors
) {
}
