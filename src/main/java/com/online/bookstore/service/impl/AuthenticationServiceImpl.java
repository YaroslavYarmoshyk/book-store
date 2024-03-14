package com.online.bookstore.service.impl;

import com.online.bookstore.dto.UserRegistrationRequestDto;
import com.online.bookstore.dto.UserResponseDto;
import com.online.bookstore.exception.RegistrationException;
import com.online.bookstore.mapper.UserMapper;
import com.online.bookstore.model.User;
import com.online.bookstore.repository.UserRepository;
import com.online.bookstore.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto registerUser(final UserRegistrationRequestDto registrationDto) {
        validateUserRegistration(registrationDto);

        final User userToSave = userMapper.toModel(registrationDto);
        userToSave.setPassword(passwordEncoder.encode(registrationDto.password()));
        final User savedUser = userRepository.save(userToSave);
        return userMapper.toDto(savedUser);
    }

    private void validateUserRegistration(final UserRegistrationRequestDto registrationDto) {
        final String email = registrationDto.email();
        if (userRepository.existsByEmail(email)) {
            throw new RegistrationException("User with email " + email + " already exists");
        }
    }
}
