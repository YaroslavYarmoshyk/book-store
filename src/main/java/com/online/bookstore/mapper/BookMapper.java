package com.online.bookstore.mapper;

import com.online.bookstore.config.mapper.MapperConfig;
import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.book.BookWithoutCategoriesDto;
import com.online.bookstore.dto.book.CreateBookRequestDto;
import com.online.bookstore.model.Book;
import com.online.bookstore.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    @Mapping(target = "categoryIds", source = "categories")
    BookDto toDto(final Book book);

    default Set<Long> mapCategoriesToIds(final Set<Category> categories) {
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }

    Book toModel(final CreateBookRequestDto requestDto);

    BookWithoutCategoriesDto toDtoWithoutCategories(final Book book);
}
