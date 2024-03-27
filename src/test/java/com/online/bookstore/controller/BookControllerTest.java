package com.online.bookstore.controller;

import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.book.CreateBookRequestDto;
import com.online.bookstore.exception.dto.ApiError;
import com.online.bookstore.utils.JacksonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static com.online.bookstore.utils.TestDataFactory.CREATE_THE_GREAT_GATSBY_BOOK;
import static com.online.bookstore.utils.TestDataFactory.INVALID_NULLABLE_CREATE_BOOK_REQUEST;
import static com.online.bookstore.utils.TestDataFactory.THE_GREAT_GATSBY_BOOK;
import static com.online.bookstore.utils.TestDataFactory.getInvalidCreateBookRequest;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Named.named;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(
        scripts = "/scripts/add-books-with-categories.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Sql(
        scripts = "/scripts/remove-books-with-categories.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS
)
class BookControllerTest {
    public static final String BOOKS_API = "/api/books";
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test book listing on the default page")
    @WithMockUser(roles = "USER")
    void getAllBooks_WithoutPageSpecification_ReturnsDefaultPage() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(BOOKS_API))
                .andExpectAll(status().isOk())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final Page<BookDto> actual = JacksonUtils.parseJsonPage(jsonResponse, BookDto.class);

        assertThat(actual.getTotalElements()).isEqualTo(15);
        assertThat(actual.getNumberOfElements()).isEqualTo(5);
        assertThat(actual.getContent())
                .contains(THE_GREAT_GATSBY_BOOK)
                .extracting(BookDto::title)
                .contains("To Kill a Mockingbird");
    }

    @Test
    @DisplayName("Test book listing on the specified page")
    @WithMockUser(roles = "USER")
    void getAllBooks_SpecifiedPage_ReturnsValidPage() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(BOOKS_API + "?page=2&size=3"))
                .andExpectAll(status().isOk())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final Page<BookDto> actual = JacksonUtils.parseJsonPage(jsonResponse, BookDto.class);

        assertThat(actual.getTotalElements()).isEqualTo(15);
        assertThat(actual.getNumberOfElements()).isEqualTo(3);
        assertThat(actual.getContent())
                .contains(THE_GREAT_GATSBY_BOOK)
                .extracting(BookDto::title)
                .containsExactly(
                        "The Great Gatsby",
                        "The Catcher in the Rye",
                        "To the Lighthouse"
                );
    }

    @Test
    @DisplayName("Test get book by existing ID")
    @WithMockUser(roles = "USER")
    void getBookById_WithExistingId_Success() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(BOOKS_API + "/4"))
                .andExpectAll(status().isOk())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final BookDto actual = JacksonUtils.parseJson(jsonResponse, BookDto.class);

        assertThat(actual).isEqualTo(THE_GREAT_GATSBY_BOOK);
    }

    @Test
    @DisplayName("Test get book by non-existing ID")
    @WithMockUser(roles = "USER")
    void getBookById_WithNonExistingId_Failure() throws Exception {
        mockMvc.perform(get("/api/books/400"))
                .andExpectAll(status().is4xxClientError())
                .andReturn();
    }

    @ParameterizedTest(name = "[{index}] = {0}")
    @MethodSource(value = "securedEndpointsArgumentsProvider")
    @DisplayName("Test secured endpoints with anonymous user")
    @WithAnonymousUser
    void testSecuredEndpoints_WhenAnonymous_ReturnsUnauthenticated(final String path,
                                                                   final HttpMethod method)
            throws Exception {
        mockMvc.perform(request(method, path))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest(name = "[{index}] = {0}")
    @MethodSource(value = "adminEndpointsArgumentsProvider")
    @DisplayName("Test admin endpoints with user role")
    @WithMockUser(roles = "USER")
    void testAdminEndpoints_WithUserRole_ReturnsForbidden(final String path,
                                                          final HttpMethod method)
            throws Exception {
        mockMvc.perform(request(method, path)
                        .content(JacksonUtils.toJson(CREATE_THE_GREAT_GATSBY_BOOK))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest(name = "[{index}] = {0}")
    @MethodSource(value = "invalidCreateBookRequestArgumentsProvider")
    @DisplayName("Test for invalid request when creating or updating book")
    @WithMockUser(roles = "ADMIN")
    void createOrUpdateBoo_InvalidRequest_ReturnsBadRequest(final CreateBookRequestDto invalidReq,
                                                            final List<String> expectedErrors)
            throws Exception {
        final String invalidJsonRequest = JacksonUtils.toJson(invalidReq);
        final MvcResult createMvcResult = mockMvc.perform(post(BOOKS_API)
                        .content(invalidJsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is4xxClientError())
                .andReturn();
        final var createJsonResponse = createMvcResult.getResponse().getContentAsString();
        final var apiErrorCreation = JacksonUtils.parseJson(createJsonResponse, ApiError.class);
        final MvcResult updateMvcResult = mockMvc.perform(put(BOOKS_API + "/4")
                        .content(invalidJsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is4xxClientError())
                .andReturn();
        final var updateJsonResponse = updateMvcResult.getResponse().getContentAsString();
        final var apiErrorUpdate = JacksonUtils.parseJson(updateJsonResponse, ApiError.class);

        assertThat(apiErrorCreation.errors())
                .containsExactlyInAnyOrderElementsOf(expectedErrors);
        assertThat(apiErrorUpdate.errors())
                .containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    @Test
    @DisplayName("Test book creation by Admin")
    @WithMockUser(roles = "ADMIN")
    @Rollback
    void createBook_ValidRequest_Success() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(post(BOOKS_API)
                        .content(JacksonUtils.toJson(CREATE_THE_GREAT_GATSBY_BOOK))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final BookDto actual = JacksonUtils.parseJson(jsonResponse, BookDto.class);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(CREATE_THE_GREAT_GATSBY_BOOK);
    }

    @Test
    @DisplayName("Test update book by Admin")
    @WithMockUser(roles = "ADMIN")
    @Rollback
    void updateBook_ValidRequest_Success() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(put(BOOKS_API + "/1")
                        .content(JacksonUtils.toJson(CREATE_THE_GREAT_GATSBY_BOOK))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final BookDto actual = JacksonUtils.parseJson(jsonResponse, BookDto.class);
        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(CREATE_THE_GREAT_GATSBY_BOOK);
    }

    @Test
    @DisplayName("Test delete book by existing ID")
    @WithMockUser(roles = "ADMIN")
    @Rollback
    void deleteBook_ByExistingId_Success() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(delete(BOOKS_API + "/4"))
                .andExpect(status().isOk())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final BookDto actual = JacksonUtils.parseJson(jsonResponse, BookDto.class);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(THE_GREAT_GATSBY_BOOK);
    }

    @Test
    @DisplayName("Test delete book by non-existing ID")
    @WithMockUser(roles = "ADMIN")
    @Rollback
    void deleteBook_ByExistingId_Failure() throws Exception {
        mockMvc.perform(delete(BOOKS_API + "/100"))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    private static Stream<Arguments> securedEndpointsArgumentsProvider() {
        return java.util.stream.Stream.of(
                Arguments.of(named("Get all books", BOOKS_API), HttpMethod.GET),
                Arguments.of(named("Get book by ID", BOOKS_API + "/1"), HttpMethod.GET),
                Arguments.of(named("Create a book", BOOKS_API), HttpMethod.POST),
                Arguments.of(named("Update book by ID", "/api/books/1"), HttpMethod.PUT),
                Arguments.of(named("Delete book by ID", "/api/books/1"), HttpMethod.DELETE)
        );
    }

    private static Stream<Arguments> adminEndpointsArgumentsProvider() {
        return java.util.stream.Stream.of(
                Arguments.of(named("Create a book", BOOKS_API), HttpMethod.POST),
                Arguments.of(named("Update book by ID", BOOKS_API + "/1"), HttpMethod.PUT),
                Arguments.of(named("Delete book by ID", BOOKS_API + "/1"), HttpMethod.DELETE)
        );
    }

    private static Stream<Arguments> invalidCreateBookRequestArgumentsProvider() {
        final List<String> expectedNullParameterErrors = List.of(
                "title must not be null",
                "price must not be null",
                "author must not be null",
                "description must not be null",
                "categoryIds must not be empty",
                "isbn must not be null"
        );
        final List<String> expectedInvalidParameterErrors = List.of(
                "author size must be between 0 and 100",
                "title size must be between 0 and 50",
                "isbn invalid ISBN",
                "price must be greater than or equal to 0",
                "categoryIds must not be empty"
        );
        return java.util.stream.Stream.of(
                Arguments.of(named("Request DTO with nullable fields",
                        INVALID_NULLABLE_CREATE_BOOK_REQUEST), expectedNullParameterErrors),
                Arguments.of(named("Request DTO with invalid fields",
                        getInvalidCreateBookRequest()), expectedInvalidParameterErrors)
        );
    }
}
