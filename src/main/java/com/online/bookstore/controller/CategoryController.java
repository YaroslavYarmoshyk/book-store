package com.online.bookstore.controller;

import com.online.bookstore.annotation.AdminAccessLevel;
import com.online.bookstore.dto.book.BookWithoutCategoriesDto;
import com.online.bookstore.dto.category.CategoryDto;
import com.online.bookstore.dto.category.CreateCategoryRequestDto;
import com.online.bookstore.service.BookService;
import com.online.bookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category management", description = "Endpoint for managing categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Get all categories")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get category by ID", description = "Get specific category")
    public CategoryDto getCategoryById(final @PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping(value = "/{id}/books")
    @Operation(
            summary = "Get all books by category ID",
            description = "Get all books by category ID"
    )
    public Page<BookWithoutCategoriesDto> getAllBooksByCategoryId(final Pageable pageable,
                                                                  final @PathVariable Long id) {
        return bookService.getAllBooksByCategoryId(pageable, id);
    }

    @AdminAccessLevel
    @PostMapping
    @Operation(
            summary = "Create a new category",
            description = "Create a new category"
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CreateCategoryRequestDto requestDto) {
        return categoryService.createCategory(requestDto);
    }

    @AdminAccessLevel
    @PutMapping(value = "/{id}")
    @Operation(
            summary = "Update category by ID",
            description = "Update category by ID"
    )
    public CategoryDto updateCategory(@PathVariable(value = "id") Long id,
                                      @Valid @RequestBody CreateCategoryRequestDto requestDto) {
        return categoryService.updateCategory(id, requestDto);
    }

    @AdminAccessLevel
    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete category by ID",
            description = "Delete category by ID"
    )
    public CategoryDto deleteCategory(final @PathVariable(value = "id") Long id) {
        return categoryService.deleteCategory(id);
    }
}
