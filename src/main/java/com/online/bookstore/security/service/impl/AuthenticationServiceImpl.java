package com.online.bookstore.security.service.impl;

import com.online.bookstore.dto.user.UserLoginRequestDto;
import com.online.bookstore.dto.user.UserLoginResponseDto;
import com.online.bookstore.dto.user.UserRegistrationRequestDto;
import com.online.bookstore.dto.user.UserResponseDto;
import com.online.bookstore.exception.RegistrationException;
import com.online.bookstore.mapper.UserMapper;
import com.online.bookstore.model.User;
import com.online.bookstore.repository.UserRepository;
import com.online.bookstore.security.service.AuthenticationService;
import com.online.bookstore.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(final UserRegistrationRequestDto registrationDto) {
        validateUserRegistration(registrationDto);

        final User userToSave = userMapper.toModel(registrationDto);
        userToSave.setPassword(passwordEncoder.encode(registrationDto.password()));
        final User savedUser = userRepository.save(userToSave);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserLoginResponseDto login(final UserLoginRequestDto loginDto) {
        final var authToken = new UsernamePasswordAuthenticationToken(
                loginDto.email(),
                loginDto.password()
        );
        final Authentication authentication = authenticationManager.authenticate(authToken);
        final String jwt = jwtService.generateToken(authentication.getName());
        return new UserLoginResponseDto(jwt);
    }

    private void validateUserRegistration(final UserRegistrationRequestDto registrationDto) {
        final String email = registrationDto.email();
        if (userRepository.existsByEmail(email)) {
            throw new RegistrationException("User with email " + email + " already exists");
        }
    }
}
