package com.online.bookstore.service.impl;

import com.online.bookstore.dto.book.BookWithoutCategoriesDto;
import com.online.bookstore.dto.category.CategoryDto;
import com.online.bookstore.dto.category.CreateCategoryRequestDto;
import com.online.bookstore.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    @Override

    public List<CategoryDto> getAllCategories(final Pageable pageable) {
        return null;
    }

    @Override
    public List<BookWithoutCategoriesDto> getAllBooksByCategoryId(final Pageable pageable,
                                                                  final Long categoryId) {
        return null;
    }

    @Override
    public CategoryDto createCategory(final CreateCategoryRequestDto requestDto) {
        return null;
    }

    @Override
    public CategoryDto updateCategory(final Long categoryId,
                                      final CreateCategoryRequestDto requestDto) {
        return null;
    }

    @Override
    public CategoryDto deleteCategory(final Long categoryId) {
        return null;
    }
}
