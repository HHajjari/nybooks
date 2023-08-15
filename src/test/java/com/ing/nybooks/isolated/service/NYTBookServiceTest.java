package com.ing.nybooks.isolated.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.nybooks.config.properties.NYTProperties;
import com.ing.nybooks.config.restClient.RestClient;
import com.ing.nybooks.model.dto.BookDto;
import com.ing.nybooks.model.external.NYTApiResponse;
import com.ing.nybooks.service.BookPublicationService;
import com.ing.nybooks.service.NYTBookService;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NYTBookServiceTest {

    @Mock
    private NYTProperties nytProperties;
    @Mock
    private RestClient restClient;
    @Mock
    private BookPublicationService bookPublicationService;
    @Mock
    private Timer timer;
    private NYTBookService nytBookService;

    @BeforeEach
    public void setup() {
        nytBookService = new NYTBookService(nytProperties, restClient, bookPublicationService, timer);
    }

    @Test
    public void testGetBooksByAuthor_Successful() throws IOException, URISyntaxException {
        NYTApiResponse response = loadTestApiResponse();
        URI uri = new URI("https://www.example.com/path?author=Diana+Gabaldon&offset=0&api-key=your-api-key");

        when(nytProperties.getScheme()).thenReturn("https");
        when(nytProperties.getHost()).thenReturn("www.example.com");
        when(nytProperties.getPath()).thenReturn("path");
        when(nytProperties.getKey()).thenReturn("your-api-key");
        when(restClient.getForObject(uri, NYTApiResponse.class)).thenReturn(response);
        when(bookPublicationService.getPublishYearByIsbn(any())).thenReturn(2023);

        Set<BookDto> books = nytBookService.getBooksByAuthor("Diana Gabaldon");

        assertEquals("Diana Gabaldon", books.stream().filter(p->p.getAuthor().equals("Diana Gabaldon")).findFirst().get().getAuthor());
    }

    @Test
    void testGetBooksByAuthorWithEmptyAuthorName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            String emptyAuthor = "";
            nytBookService.getBooksByAuthor(emptyAuthor);
        });
        assertEquals("Author name cannot be empty", exception.getMessage());
    }

    @Test
    void testGetBooksByAuthorWithNullAuthorName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            String nullAuthor = null;
            nytBookService.getBooksByAuthor(nullAuthor);
        });
        assertEquals("Author name cannot be empty", exception.getMessage());
    }
    private NYTApiResponse loadTestApiResponse() throws IOException {
        // Load test JSON data from the resources
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream jsonStream = getClass().getResourceAsStream("/nytApiResponse.json");
        return objectMapper.readValue(jsonStream, NYTApiResponse.class);
    }
}
