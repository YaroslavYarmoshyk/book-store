package com.online.bookstore.security.service;

import com.online.bookstore.dto.user.UserLoginRequestDto;
import com.online.bookstore.dto.user.UserLoginResponseDto;
import com.online.bookstore.dto.user.UserRegistrationRequestDto;
import com.online.bookstore.dto.user.UserResponseDto;

public interface AuthenticationService {
    UserResponseDto register(final UserRegistrationRequestDto registrationDto);

    UserLoginResponseDto login(final UserLoginRequestDto loginDto);
}
