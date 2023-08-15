package com.ing.nybooks.isolated.service;

import com.ing.nybooks.model.dto.BookDto;
import com.ing.nybooks.service.NYTBookService;
import com.ing.nybooks.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private NYTBookService nytBookService;

    private SearchService searchService;

    @BeforeEach
    public void setup() {
        searchService = new SearchService(nytBookService);
    }

    @Test
    public void testGetBooksByAuthor() {
        Set<BookDto> mockResponse = new HashSet<>();

        when(nytBookService.getBooksByAuthor(anyString())).thenReturn(mockResponse);

        Set<BookDto> result = searchService.getBooksByAuthor("Diana Gabaldon");

        assertEquals(mockResponse, result);
    }

    @Test
    public void testGetBooksByAuthorAndYear() {
        Set<BookDto> mockResponse = new HashSet<>();

        when(nytBookService.getBooksByAuthor(anyString())).thenReturn(mockResponse);

        Set<BookDto> result = searchService.getBooksByAuthorAndYear("Diana Gabaldon", List.of(2020,2021));

        assertEquals(mockResponse, result);
    }
}
