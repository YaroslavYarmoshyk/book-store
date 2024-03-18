package com.online.bookstore.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotNull @Size(max = 200) String name,
        @NotNull @Size(max = 10_000) String description) {
}
