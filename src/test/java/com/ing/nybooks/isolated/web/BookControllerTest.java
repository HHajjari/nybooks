package com.ing.nybooks.isolated.web;
import com.ing.nybooks.model.dto.BookDto;
import com.ing.nybooks.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ing.nybooks.model.Const.ME_BOOKS_LIST;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    private Set<BookDto> mockBooks;

    @BeforeEach
    public void setup() {
        mockBooks = new HashSet<>();

        BookDto book1 = new BookDto();
        book1.setName("Book 1");
        book1.setPublisher("Publisher A");
        book1.setAuthor("Author X");
        book1.setYear(2021);
        mockBooks.add(book1);

        BookDto book2 = new BookDto();
        book2.setName("Book 2");
        book2.setPublisher("Publisher B");
        book2.setAuthor("Author Y");
        book2.setYear(2022);
        mockBooks.add(book2);
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksByAuthorAuthorized() throws Exception {
        when(searchService.getBooksByAuthor("AuthorName")).thenReturn(mockBooks);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "AuthorName")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(mockBooks.size()))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksByAuthorAndYearAuthorized() throws Exception {
        when(searchService.getBooksByAuthorAndYear("AuthorName", List.of(2022))).thenReturn(mockBooks);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "AuthorName")
                        .param("year", "2022")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(mockBooks.size()))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksInvalidInput() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Expecting a bad request status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksInvalidAuthor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksInvalidYear() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("year", "invalidYear")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksInvalidAuthorAndYear() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "")
                        .param("year", "invalidYear")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksEmptyAuthor() throws Exception {
        when(searchService.getBooksByAuthor("")).thenReturn(new HashSet<>());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksEmptyYear() throws Exception {
        when(searchService.getBooksByAuthorAndYear("AuthorName", Collections.emptyList())).thenReturn(new HashSet<>());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "AuthorName")
                        .param("year", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksInvalidAuthorAndValidYear() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "")
                        .param("year", "2022")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Validation error")) // Custom error message for invalid author
                .andDo(print());
    }

    @Test
    public void testSearchBooksByAuthorUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "AuthorName")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void testSearchBooksByAuthorAndYearUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "AuthorName")
                        .param("year", "2022")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksServiceError() throws Exception {
        when(searchService.getBooksByAuthor("AuthorName")).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "AuthorName")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()) // Expecting a 500 Internal Server Error status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("An unexpected error occurred")) // Generic error message
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksNoResults() throws Exception {
        when(searchService.getBooksByAuthor("NonExistentAuthor")).thenReturn(new HashSet<>());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "NonExistentAuthor")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0)) // Expecting an empty array
                .andDo(print());
    }
}