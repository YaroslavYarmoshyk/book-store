package com.online.bookstore.controller;

import com.online.bookstore.dto.UserRegistrationRequestDto;
import com.online.bookstore.dto.UserResponseDto;
import com.online.bookstore.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register")
    public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto requestDto) {
        return authenticationService.registerUser(requestDto);
    }
}
