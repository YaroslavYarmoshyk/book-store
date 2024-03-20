package com.online.bookstore.dto.user;

import com.online.bookstore.annotation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@FieldMatch(
        first = "password",
        second = "repeatPassword",
        message = "the password fields must match"
)
public record UserRegistrationRequestDto(
        @NotNull @Email String email,
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String password,
        @NotNull String repeatPassword,
        String shippingAddress
) {
}
