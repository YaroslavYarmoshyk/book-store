package com.online.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.online.bookstore.dto.category.CategoryDto;
import com.online.bookstore.dto.category.CreateCategoryRequestDto;
import com.online.bookstore.exception.EntityNotFoundException;
import com.online.bookstore.mapper.CategoryMapper;
import com.online.bookstore.model.Category;
import com.online.bookstore.repository.CategoryRepository;
import com.online.bookstore.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(value = MockitoExtension.class)
class CategoryServiceImplTest {
    public static final Long EXISTING_ID = 1L;
    public static final Long NON_EXISTING_ID = 100L;
    @Spy
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("Test create category")
    void createCategory_Success() {
        final CreateCategoryRequestDto requestDto = mock(CreateCategoryRequestDto.class);
        final Category category = mock(Category.class);
        final CategoryDto categoryDto = mock(CategoryDto.class);
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        final CategoryDto actual = categoryService.createCategory(requestDto);

        assertThat(actual).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Test update category by existing ID")
    void updateCategory_ExistingId_Success() {
        final CreateCategoryRequestDto requestDto = mock(CreateCategoryRequestDto.class);
        final Category category = mock(Category.class);
        final CategoryDto categoryDto = mock(CategoryDto.class);
        when(categoryRepository.existsById(EXISTING_ID)).thenReturn(true);
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category.setId(EXISTING_ID))).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        final CategoryDto actual = categoryService.updateCategory(EXISTING_ID, requestDto);

        assertThat(actual).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Test update category by non-existing ID")
    void updateCategory_NonExistingId_ThrowsException() {
        final CreateCategoryRequestDto createCategoryRequest = mock(CreateCategoryRequestDto.class);
        when(categoryRepository.existsById(NON_EXISTING_ID)).thenReturn(false);

        assertThatThrownBy(
                () -> categoryService.updateCategory(NON_EXISTING_ID, createCategoryRequest)
        )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(
                        "Category by id: " + NON_EXISTING_ID + " was not found"
                );
    }

    @Test
    @DisplayName("Test delete category by ID")
    void deleteCategory_Success() {
        final CategoryDto categoryDto = mock(CategoryDto.class);
        doReturn(categoryDto).when(categoryService).getCategoryById(EXISTING_ID);

        final CategoryDto actual = categoryService.deleteCategory(EXISTING_ID);

        assertThat(actual).isEqualTo(categoryDto);
        verify(categoryRepository).deleteById(EXISTING_ID);
    }

    @Test
    @DisplayName("Test get category by existing ID")
    void getCategoryById_ExistingId_Success() {
        final Category category = mock(Category.class);
        final CategoryDto categoryDto = mock(CategoryDto.class);
        when(categoryRepository.findById(EXISTING_ID)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        final CategoryDto actual = categoryService.getCategoryById(EXISTING_ID);

        assertThat(actual).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Test get category by non-existing ID")
    void getCategoryById_NonExistingId_ThrowsException() {
        when(categoryRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryById(NON_EXISTING_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category by id: " + NON_EXISTING_ID + " was not found");
    }

    @Test
    @DisplayName("Test get all categories")
    void getAllCategories_Success() {
        final Category category1 = mock(Category.class);
        final Category category2 = mock(Category.class);
        final CategoryDto categoryDto1 = mock(CategoryDto.class);
        final CategoryDto categoryDto2 = mock(CategoryDto.class);
        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));
        when(categoryMapper.toDto(List.of(category1, category2)))
                .thenReturn(List.of(categoryDto1, categoryDto2));

        final List<CategoryDto> actual = categoryService.getAllCategories();

        assertThat(actual).containsExactlyInAnyOrder(categoryDto1, categoryDto2);
    }
}
