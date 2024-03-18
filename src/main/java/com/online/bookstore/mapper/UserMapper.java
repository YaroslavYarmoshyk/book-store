package com.online.bookstore.mapper;

import com.online.bookstore.config.mapper.MapperConfig;
import com.online.bookstore.dto.user.UserRegistrationRequestDto;
import com.online.bookstore.dto.user.UserResponseDto;
import com.online.bookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toDto(final User user);

    User toModel(final UserRegistrationRequestDto registrationDto);
}
