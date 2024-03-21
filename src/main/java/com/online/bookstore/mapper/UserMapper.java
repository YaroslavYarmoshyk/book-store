package com.online.bookstore.mapper;

import com.online.bookstore.config.mapper.MapperConfig;
import com.online.bookstore.dto.user.UserRegistrationRequestDto;
import com.online.bookstore.dto.user.UserResponseDto;
import com.online.bookstore.model.User;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toDto(final User user);

    User toModel(final UserRegistrationRequestDto registrationDto);

    @Named(value = "userFromId")
    default User getUserById(final Long userId) {
        return Optional.ofNullable(userId)
                .map(id -> new User().setId(id))
                .orElse(null);
    }
}
