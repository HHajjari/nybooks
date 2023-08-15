package com.ing.nybooks.real;
import com.ing.nybooks.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.ing.nybooks.model.Const.ME_BOOKS_LIST;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SearchService searchService;
    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksByAuthorAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "Rebecca Yarros")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(greaterThan(0)))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksByAuthorAndYearAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "Rebecca Yarros")
                        .param("year", "2023")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(greaterThan(0)))
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
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "Rebecca Yarros")
                        .param("year", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThan(0)))
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
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andDo(print());
    }

    @Test
    public void testSearchBooksByAuthorUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "Rebecca Yarros")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void testSearchBooksByAuthorAndYearUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "Rebecca Yarros")
                        .param("year", "2022")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "operator1")
    public void testSearchBooksNoResults() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(ME_BOOKS_LIST)
                        .param("author", "NonExistentAuthor")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());
    }
}