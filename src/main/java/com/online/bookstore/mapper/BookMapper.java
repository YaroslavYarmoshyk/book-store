package com.online.bookstore.mapper;

import com.online.bookstore.config.MapperConfig;
import com.online.bookstore.dto.BookDto;
import com.online.bookstore.dto.CreateBookRequestDto;
import com.online.bookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(final Book book);

    Book toModel(final CreateBookRequestDto requestDto);
}
