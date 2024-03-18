package com.online.bookstore.mapper;

import com.online.bookstore.config.mapper.MapperConfig;
import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.book.CreateBookRequestDto;
import com.online.bookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(final Book book);

    Book toModel(final CreateBookRequestDto requestDto);
}
