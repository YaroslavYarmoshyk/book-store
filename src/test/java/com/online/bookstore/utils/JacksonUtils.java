package com.online.bookstore.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.online.bookstore.config.CustomPageImpl;
import com.online.bookstore.exception.SystemException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

public final class JacksonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static <T> T parseJson(final String jsonContent, final Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonContent, clazz);
        } catch (final JsonProcessingException e) {
            throw new SystemException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static <T> String toJson(final T object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new SystemException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static <T> Page<T> parseJsonPage(final String jsonContent, final Class<T> clazz) {
        try {
            final JavaType type = OBJECT_MAPPER.getTypeFactory()
                    .constructParametricType(CustomPageImpl.class, clazz);
            return OBJECT_MAPPER.readValue(jsonContent, type);
        } catch (final JsonProcessingException e) {
            throw new SystemException(
                    "Cannot parse content",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
