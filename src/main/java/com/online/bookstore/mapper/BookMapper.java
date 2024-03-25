package com.online.bookstore.mapper;

import com.online.bookstore.config.mapper.MapperConfig;
import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.book.BookWithoutCategoriesDto;
import com.online.bookstore.dto.book.CreateBookRequestDto;
import com.online.bookstore.model.Book;
import com.online.bookstore.model.Category;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", source = "categories")
    BookDto toDto(final Book book);

    @Mapping(target = "categoryIds", source = "categories")
    List<BookDto> toDto(final List<Book> books);

    default Set<Long> mapCategoriesToIds(final Set<Category> categories) {
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "categories", source = "categoryIds")
    Book toModel(final CreateBookRequestDto requestDto);

    default Set<Category> mapIdsToCategories(final Set<Long> categoryIds) {
        return categoryIds.stream()
                .map(id -> (Category) new Category().setId(id))
                .collect(Collectors.toSet());
    }

    BookWithoutCategoriesDto toDtoWithoutCategories(final Book book);

    @Named("bookFromId")
    default Book bookFromId(final Long id) {
        return Optional.ofNullable(id)
                .map(bookId -> (Book) new Book().setId(bookId))
                .orElse(null);
    }
}
