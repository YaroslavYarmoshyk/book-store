package com.online.bookstore.service;

import com.online.bookstore.dto.category.CategoryDto;
import com.online.bookstore.dto.category.CreateCategoryRequestDto;
import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(final CreateCategoryRequestDto requestDto);

    CategoryDto updateCategory(final Long categoryId,
                               final CreateCategoryRequestDto requestDto);

    CategoryDto deleteCategory(final Long categoryId);

    CategoryDto getCategoryById(final Long categoryId);

    List<CategoryDto> getAllCategories();
}
