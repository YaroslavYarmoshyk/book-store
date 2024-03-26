package com.online.bookstore.controller;

import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.utils.JacksonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static com.online.bookstore.utils.TestDataFactory.THE_GREAT_GATSBY;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @DisplayName("Test unauthenticated access")
    void getAllBooks_WhenUnauthenticated_ThrowsException() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test book listing default page")
    @WithMockUser(roles = "USER")
    void getAllBooks_WithoutPageSpecification_ReturnsDefaultPage() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get("/api/books"))
                .andExpectAll(status().isOk())
                .andReturn();
        final String response = mvcResult.getResponse().getContentAsString();
        final Page<BookDto> bookPage = JacksonUtils.parsePage(response, BookDto.class);

        assertThat(bookPage.getTotalElements()).isEqualTo(15);
        assertThat(bookPage.getNumberOfElements()).isEqualTo(5);
        assertThat(bookPage.getContent())
                .contains(THE_GREAT_GATSBY)
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
        final Page<BookDto> bookPage = JacksonUtils.parsePage(response, BookDto.class);

        assertThat(bookPage.getTotalElements()).isEqualTo(15);
        assertThat(bookPage.getNumberOfElements()).isEqualTo(3);
        assertThat(bookPage.getContent())
                .contains(THE_GREAT_GATSBY)
                .extracting(BookDto::title)
                .containsExactly(
                        "The Great Gatsby",
                        "The Catcher in the Rye",
                        "To the Lighthouse"
                );
    }
}
