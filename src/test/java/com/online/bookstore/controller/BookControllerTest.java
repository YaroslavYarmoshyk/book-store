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
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test book listing default page")
    @WithMockUser(roles = "USER")
    void getAllBooks_WithoutPageSpecification_ReturnsDefaultPage() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get("/api/books"))
                .andExpectAll(status().isOk())
                .andReturn();
        final String response = mvcResult.getResponse().getContentAsString();
        final Page<BookDto> bookPage = JacksonUtils.parseJsonPage(response, BookDto.class);

        assertThat(bookPage.getTotalElements()).isEqualTo(15);
        assertThat(bookPage.getNumberOfElements()).isEqualTo(5);
        assertThat(bookPage.getContent())
                .contains(THE_GREAT_GATSBY_BOOK)
                .extracting(BookDto::title)
                .contains("To Kill a Mockingbird");
    }

    @Test
    @DisplayName("Test book listing specified page")
    @WithMockUser(roles = "USER")
    void getAllBooks_SpecifiedPage_ReturnsValidPage() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get("/api/books?page=2&size=3"))
                .andExpectAll(status().isOk())
                .andReturn();
        final String response = mvcResult.getResponse().getContentAsString();
        final Page<BookDto> bookPage = JacksonUtils.parseJsonPage(response, BookDto.class);

        assertThat(bookPage.getTotalElements()).isEqualTo(15);
        assertThat(bookPage.getNumberOfElements()).isEqualTo(3);
        assertThat(bookPage.getContent())
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
        final MvcResult mvcResult = mockMvc.perform(get("/api/books/4"))
                .andExpectAll(status().isOk())
                .andReturn();
        final String response = mvcResult.getResponse().getContentAsString();
        final BookDto actual = JacksonUtils.parseJson(response, BookDto.class);

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
    @DisplayName("Test for an invalid request when creating or updating a book")
    @WithMockUser(roles = "ADMIN")
    void createOrUpdateBoo_InvalidRequest_ReturnsBadRequest(final CreateBookRequestDto invalidReq,
                                                            final List<String> expectedErrors)
            throws Exception {
        final String invalidJsonRequest = JacksonUtils.toJson(invalidReq);
        final MvcResult createMvcResult = mockMvc.perform(post("/api/books")
                        .content(invalidJsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is4xxClientError())
                .andReturn();
        final ApiError apiErrorDuringCreation = JacksonUtils.parseJson(
                createMvcResult.getResponse().getContentAsString(),
                ApiError.class
        );
        final MvcResult updateMvcResult = mockMvc.perform(put("/api/books/4")
                        .content(invalidJsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is4xxClientError())
                .andReturn();
        final ApiError apiErrorDuringUpdate = JacksonUtils.parseJson(
                updateMvcResult.getResponse().getContentAsString(),
                ApiError.class
        );

        assertThat(apiErrorDuringCreation.errors())
                .containsExactlyInAnyOrderElementsOf(expectedErrors);
        assertThat(apiErrorDuringUpdate.errors())
                .containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    private static Stream<Arguments> securedEndpointsArgumentsProvider() {
        return java.util.stream.Stream.of(
                Arguments.of(named("Get all books", "/api/books"), HttpMethod.GET),
                Arguments.of(named("Get book by ID", "/api/books/1"), HttpMethod.GET),
                Arguments.of(named("Create a book", "/api/books"), HttpMethod.POST),
                Arguments.of(named("Update book by ID", "/api/books/1"), HttpMethod.PUT),
                Arguments.of(named("Delete book by ID", "/api/books/1"), HttpMethod.DELETE)
        );
    }

    private static Stream<Arguments> adminEndpointsArgumentsProvider() {
        return java.util.stream.Stream.of(
                Arguments.of(named("Create a book", "/api/books"), HttpMethod.POST),
                Arguments.of(named("Update book by ID", "/api/books/1"), HttpMethod.PUT),
                Arguments.of(named("Delete book by ID", "/api/books/1"), HttpMethod.DELETE)
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
