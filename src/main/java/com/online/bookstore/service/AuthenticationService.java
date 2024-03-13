package com.online.bookstore.service;

import com.online.bookstore.dto.UserRegistrationRequestDto;
import com.online.bookstore.dto.UserResponseDto;

public interface AuthenticationService {

    UserResponseDto registerUser(final UserRegistrationRequestDto registrationDto);

}
