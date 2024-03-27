package com.online.bookstore.controller;

import com.online.bookstore.dto.book.BookWithoutCategoriesDto;
import com.online.bookstore.dto.category.CategoryDto;
import com.online.bookstore.dto.category.CreateCategoryRequestDto;
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

import static com.online.bookstore.utils.TestDataFactory.CREATE_HISTORICAL_FICTION_CATEGORY;
import static com.online.bookstore.utils.TestDataFactory.FICTION_CATEGORY;
import static com.online.bookstore.utils.TestDataFactory.INVALID_NULLABLE_CREATE_CATEGORY_REQUEST;
import static com.online.bookstore.utils.TestDataFactory.getInvalidCreateCategoryRequest;
import static org.assertj.core.api.Assertions.assertThat;
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
class CategoryControllerTest {
    private static final String CATEGORIES_API = "/api/categories";
    private static final String FIRST_CATEGORY_API = "/api/categories/1";
    private static final String SECOND_CATEGORY_API = "/api/categories/2";
    private static final String NON_EXISTING_CATEGORY_API = "/api/categories/100";
    private static final String BOOKS_BY_THIRD_CATEGORY_API = "/api/categories/3/books";
    private static final String BOOKS_BY_NON_EXISTING_CATEGORY_API = "/api/categories/100/books";
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test get all categories")
    @WithMockUser(roles = "USER")
    void getAllCategories_ReturnsCollectionOfCategories() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(CATEGORIES_API))
                .andExpectAll(status().isOk())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final var actual = JacksonUtils.parseJsonList(jsonResponse, CategoryDto.class);

        assertThat(actual).hasSize(10);
        assertThat(actual).contains(FICTION_CATEGORY);
    }

    @Test
    @DisplayName("Test get category by existing ID")
    @WithMockUser(roles = "USER")
    void getCategoryById_WithExistingId_Success() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(FIRST_CATEGORY_API))
                .andExpectAll(status().isOk())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final CategoryDto actual = JacksonUtils.parseJson(jsonResponse, CategoryDto.class);

        assertThat(actual).isEqualTo(FICTION_CATEGORY);
    }

    @Test
    @DisplayName("Test get category by non-existing ID")
    @WithMockUser(roles = "USER")
    void getCategoryById_WithNonExistingId_Failure() throws Exception {
        mockMvc.perform(get(NON_EXISTING_CATEGORY_API))
                .andExpectAll(status().is4xxClientError())
                .andReturn();
    }

    @Test
    @DisplayName("Test get all books by existing category ID")
    @WithMockUser(roles = "USER")
    void getAllBooksByCategoryId_WithExistingId_ReturnsPageWithContents() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(BOOKS_BY_THIRD_CATEGORY_API))
                .andExpectAll(status().isOk())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final var actual = JacksonUtils.parseJsonPage(jsonResponse, BookWithoutCategoriesDto.class);

        assertThat(actual.getTotalElements()).isEqualTo(2);
        assertThat(actual.getNumberOfElements()).isEqualTo(2);
        assertThat(actual.getContent())
                .extracting(BookWithoutCategoriesDto::id)
                .containsExactly(2L, 8L);
    }

    @Test
    @DisplayName("Test get all books by non-existing category ID")
    @WithMockUser(roles = "USER")
    void getAllBooksByCategoryId_WithNonExistingId_ReturnsEmptyPage() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(BOOKS_BY_NON_EXISTING_CATEGORY_API))
                .andExpectAll(status().isOk())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final var actual = JacksonUtils.parseJsonPage(jsonResponse, BookWithoutCategoriesDto.class);

        assertThat(actual.getTotalElements()).isEqualTo(0);
        assertThat(actual.getNumberOfElements()).isEqualTo(0);
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Test delete category by existing ID")
    @WithMockUser(roles = "ADMIN")
    @Rollback
    void deleteCategory_WithExistingId_Success() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(delete(FIRST_CATEGORY_API))
                .andExpectAll(status().isOk())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final CategoryDto actual = JacksonUtils.parseJson(jsonResponse, CategoryDto.class);

        assertThat(actual).usingRecursiveAssertion().isEqualTo(FICTION_CATEGORY);
    }

    @Test
    @DisplayName("Test category creation by Admin")
    @WithMockUser(roles = "ADMIN")
    @Rollback
    void createCategory_ValidRequest_Success() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(post(CATEGORIES_API)
                        .content(JacksonUtils.toJson(CREATE_HISTORICAL_FICTION_CATEGORY))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final CategoryDto actual = JacksonUtils.parseJson(jsonResponse, CategoryDto.class);

        assertThat(actual)
                .returns(CREATE_HISTORICAL_FICTION_CATEGORY.name(), CategoryDto::name)
                .returns(CREATE_HISTORICAL_FICTION_CATEGORY.description(), CategoryDto::description)
                .extracting(CategoryDto::id).isNotNull();
    }

    @Test
    @DisplayName("Test update category by Admin")
    @WithMockUser(roles = "ADMIN")
    @Rollback
    void updateCategory_ValidRequest_Success() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(put(SECOND_CATEGORY_API)
                        .content(JacksonUtils.toJson(CREATE_HISTORICAL_FICTION_CATEGORY))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        final String jsonResponse = mvcResult.getResponse().getContentAsString();
        final CategoryDto actual = JacksonUtils.parseJson(jsonResponse, CategoryDto.class);

        assertThat(actual)
                .returns(CREATE_HISTORICAL_FICTION_CATEGORY.name(), CategoryDto::name)
                .returns(CREATE_HISTORICAL_FICTION_CATEGORY.description(), CategoryDto::description)
                .returns(2L, CategoryDto::id);
    }

    @ParameterizedTest(name = "[{index}] = {0}")
    @MethodSource(value = "invalidCategoryRequestArgumentsProvider")
    @DisplayName("Test for invalid request when creating or updating category")
    @WithMockUser(roles = "ADMIN")
    void createOrUpdateCategory_InvalidRequest_BadRequest(final CreateCategoryRequestDto invalidReq,
                                                                 final List<String> expectedErrors)
            throws Exception {
        final String invalidJsonRequest = JacksonUtils.toJson(invalidReq);
        final MvcResult createMvcResult = mockMvc.perform(post(CATEGORIES_API)
                        .content(invalidJsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is4xxClientError())
                .andReturn();
        final var createJsonResponse = createMvcResult.getResponse().getContentAsString();
        final var apiErrorCreation = JacksonUtils.parseJson(createJsonResponse, ApiError.class);
        final MvcResult updateMvcResult = mockMvc.perform(put(FIRST_CATEGORY_API)
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
                        .content(JacksonUtils.toJson(CREATE_HISTORICAL_FICTION_CATEGORY))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private static Stream<Arguments> invalidCategoryRequestArgumentsProvider() {
        final List<String> expectedNullParameterErrors = List.of(
                "name must not be null",
                "description must not be null"
        );
        final List<String> expectedInvalidParameterErrors = List.of(
                "name size must be between 0 and 200",
                "description size must be between 0 and 10000"
        );
        return java.util.stream.Stream.of(
                Arguments.of(named("Request DTO with nullable fields",
                        INVALID_NULLABLE_CREATE_CATEGORY_REQUEST), expectedNullParameterErrors),
                Arguments.of(named("Request DTO with invalid fields",
                        getInvalidCreateCategoryRequest()), expectedInvalidParameterErrors)
        );
    }

    private static Stream<Arguments> securedEndpointsArgumentsProvider() {
        return java.util.stream.Stream.of(
                Arguments.of(named("Get all categories",
                        CATEGORIES_API), HttpMethod.GET),
                Arguments.of(named("Get category by ID",
                        FIRST_CATEGORY_API), HttpMethod.GET),
                Arguments.of(named("Get all books by category ID",
                        FIRST_CATEGORY_API), HttpMethod.GET),
                Arguments.of(named("Create a category",
                        CATEGORIES_API), HttpMethod.POST),
                Arguments.of(named("Update category by ID",
                        FIRST_CATEGORY_API), HttpMethod.PUT),
                Arguments.of(named("Delete category by ID",
                        FIRST_CATEGORY_API), HttpMethod.DELETE)
        );
    }

    private static Stream<Arguments> adminEndpointsArgumentsProvider() {
        return java.util.stream.Stream.of(
                Arguments.of(named("Create a category",
                        CATEGORIES_API), HttpMethod.POST),
                Arguments.of(named("Update category by ID",
                        FIRST_CATEGORY_API), HttpMethod.PUT),
                Arguments.of(named("Delete category by ID",
                        FIRST_CATEGORY_API), HttpMethod.DELETE)
        );
    }

}
