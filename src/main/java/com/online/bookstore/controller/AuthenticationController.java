package com.online.bookstore.controller;

import com.online.bookstore.dto.UserRegistrationRequestDto;
import com.online.bookstore.dto.UserResponseDto;
import com.online.bookstore.service.AuthenticationService;
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
        return authenticationService.registerUser(requestDto);
    }
}
