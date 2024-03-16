package com.online.bookstore.security.service;

import com.online.bookstore.dto.UserLoginRequestDto;
import com.online.bookstore.dto.UserLoginResponseDto;
import com.online.bookstore.dto.UserRegistrationRequestDto;
import com.online.bookstore.dto.UserResponseDto;

public interface AuthenticationService {

    UserResponseDto register(final UserRegistrationRequestDto registrationDto);

    UserLoginResponseDto login(final UserLoginRequestDto loginDto);
}
