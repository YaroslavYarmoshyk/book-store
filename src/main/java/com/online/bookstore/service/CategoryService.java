package com.online.bookstore.service;

import com.online.bookstore.dto.BookWithoutCategoriesDto;
import com.online.bookstore.dto.CategoryDto;
import com.online.bookstore.dto.CreateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    List<CategoryDto> getAllCategories(final Pageable pageable);

    List<BookWithoutCategoriesDto> getAllBooksByCategoryId(final Pageable pageable,
                                                           final Long categoryId);

    CategoryDto createCategory(final CreateCategoryRequestDto requestDto);

    CategoryDto updateCategory(final Long categoryId,
                               final CreateCategoryRequestDto requestDto);

    CategoryDto deleteCategory(final Long categoryId);
}
