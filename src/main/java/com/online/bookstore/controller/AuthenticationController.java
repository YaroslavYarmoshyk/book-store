package com.online.bookstore.controller;

import com.online.bookstore.dto.user.UserLoginRequestDto;
import com.online.bookstore.dto.user.UserLoginResponseDto;
import com.online.bookstore.dto.user.UserRegistrationRequestDto;
import com.online.bookstore.dto.user.UserResponseDto;
import com.online.bookstore.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management", description = "Endpoint for registration and login")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register")
    @Operation(summary = "Register a new user", description = "Register a new user")
    public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto requestDto) {
        return authenticationService.register(requestDto);
    }

    @PostMapping(value = "/login")
    @Operation(summary = "Login a user", description = "Login a user")
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.login(requestDto);
    }
}
