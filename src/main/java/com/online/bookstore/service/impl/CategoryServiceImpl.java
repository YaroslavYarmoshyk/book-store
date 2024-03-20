package com.online.bookstore.service.impl;

import com.online.bookstore.dto.category.CategoryDto;
import com.online.bookstore.dto.category.CreateCategoryRequestDto;
import com.online.bookstore.exception.EntityNotFoundException;
import com.online.bookstore.mapper.CategoryMapper;
import com.online.bookstore.model.Category;
import com.online.bookstore.repository.CategoryRepository;
import com.online.bookstore.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(final CreateCategoryRequestDto requestDto) {
        final Category savedCategory = categoryRepository.save(categoryMapper.toModel(requestDto));
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(final Long categoryId,
                                      final CreateCategoryRequestDto requestDto) {
        if (categoryRepository.existsById(categoryId)) {
            final Category categoryToSave = categoryMapper.toModel(requestDto)
                    .setId(categoryId);
            final Category savedCategory = categoryRepository.save(categoryToSave);
            return categoryMapper.toDto(savedCategory);
        }
        throw new EntityNotFoundException("Category by id: " + categoryId + " was not found");
    }

    @Override
    public CategoryDto deleteCategory(final Long categoryId) {
        final CategoryDto categoryById = getCategoryById(categoryId);
        categoryRepository.deleteById(categoryId);
        return categoryById;
    }

    @Override
    public CategoryDto getCategoryById(final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Category by id: " + categoryId + " was not found"
                ));
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryMapper.toDto(categoryRepository.findAll());
    }
}
