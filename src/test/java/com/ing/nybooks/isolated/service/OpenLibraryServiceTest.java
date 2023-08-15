package com.ing.nybooks.isolated.service;

import com.ing.nybooks.service.OpenLibraryService;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.ing.nybooks.config.properties.OpenLibraryProperties;
import com.ing.nybooks.config.restClient.RestClient;
import com.ing.nybooks.model.external.OpenLibraryApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OpenLibraryServiceTest {
    @Mock
    private OpenLibraryProperties openLibraryProperties;
    @Mock
    private RestClient restClient;
    @Mock
    private Timer timer;
    private OpenLibraryService openLibraryService;
    @BeforeEach
    public void setup() {
        openLibraryService = new OpenLibraryService(openLibraryProperties, restClient, timer);
    }

    // Parameterized test using @CsvSource
    @ParameterizedTest
    @CsvSource({
            "2000-01-01, 2000",
            "1990 Jun, 1990",
            "2019, 2019",
            "1/2/2000, 2000",
            "August 2001, 2001",
            "null, -2",
            "'', -2",
            "abs2000def 12, -2",
    })
    public void testGetPublishYearByIsbn_Successful(String publishDate, int expectedPublicationYear) {
        OpenLibraryApiResponse response = createResponse(publishDate);

        when(restClient.getForObject(anyString(), eq(OpenLibraryApiResponse.class))).thenReturn(response);
        when(openLibraryProperties.getUrl()).thenReturn("https://openlibrary.com/api/%s");

        int publicationYear = openLibraryService.getPublishYearByIsbn(Set.of("123456789"));

        verify(restClient, times(1)).getForObject(anyString(), eq(OpenLibraryApiResponse.class));
        assertEquals(expectedPublicationYear, publicationYear);
    }
    private OpenLibraryApiResponse createResponse(String publishDate) {
        OpenLibraryApiResponse response = new OpenLibraryApiResponse();
        response.setPublish_date(publishDate);
        return response;
    }
}

