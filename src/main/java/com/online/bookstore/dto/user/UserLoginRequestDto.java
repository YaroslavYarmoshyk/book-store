package com.online.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserLoginRequestDto(
        @Email @NotNull String email,
        @NotNull String password
) {
}
