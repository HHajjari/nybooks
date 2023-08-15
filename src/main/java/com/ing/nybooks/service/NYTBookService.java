package com.ing.nybooks.service;

import com.ing.nybooks.config.properties.NYTProperties;
import com.ing.nybooks.config.restClient.RestClient;
import com.ing.nybooks.model.dto.BookDto;
import com.ing.nybooks.model.external.NYTApiResponse;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ing.nybooks.model.Const.*;

/**
 * Service class for interacting with the NYTimes API to retrieve books by author.
 */
@Service
public class NYTBookService {
    private static final Logger logger = LoggerFactory.getLogger(NYTBookService.class);
    private final NYTProperties nytProperties;
    private final RestClient restClient;
    private final BookPublicationService bookPublicationService;
    private final Timer timer;

    /**
     * Constructor for NYTimesBookService.
     *
     * @param nytProperties         Configuration properties for NYTimes API.
     * @param restClient            REST client for making API calls.
     * @param bookPublicationService Service for retrieving book publication year.
     * @param timer                 Timer recording metrics.
     */
    public NYTBookService(NYTProperties nytProperties, RestClient restClient,
                          BookPublicationService bookPublicationService, @Qualifier("nytBookApiCallTimer") Timer timer) {
        this.nytProperties = nytProperties;
        this.restClient = restClient;
        this.bookPublicationService = bookPublicationService;
        this.timer = timer;
    }

    /**
     * Retrieve books by author and cache the result.
     *
     * @param author The author's name.
     * @return A BookResponseDto containing book information.
     */
    @Cacheable(cacheNames = GET_BOOKS_BY_AUTHOR, key = "#author")
    public Set<BookDto> getBooksByAuthor(String author) {
        logger.info("[getBooksByAuthor] Retrieving books by author: {}", author);

        // Validating and encoding the author name, then creating Uri
        validateAuthorName(author);
        String sanitizedAuthorName = sanitizeAndEncode(author);
        Set<BookDto> allBooks = new HashSet<>();
        int recordsPerPage = 20;
        URI uri = buildApiUri(sanitizedAuthorName, 0);
        NYTApiResponse firstResponse = restClient.getForObject(uri, NYTApiResponse.class);

        if (firstResponse != null && firstResponse.getNum_results() > 0) {
            int numPages = (int) Math.ceil((double) firstResponse.getNum_results() / recordsPerPage);

            for (int page = 0; page < numPages; page++) {
                uri = buildApiUri(sanitizedAuthorName, page * recordsPerPage);

                Timer.Sample sample = Timer.start();
                NYTApiResponse response = restClient.getForObject(uri, NYTApiResponse.class);
                timer.record(() -> sample.stop(timer) / 1000000);

                if (response != null && response.getResults() != null) {
                    allBooks.addAll(mapToBookResponseDto(response));
                }
                logger.info("[getBooksByAuthor] Retrieved {} books for author: {} offset {}", allBooks.size(), author, page);
            }
        }
        logger.info("[getBooksByAuthor] Retrieved {} books for author: {}", allBooks.size(), author);

        return allBooks;
    }

    /**
     * Map NYTimesApiResponse to BookResponseDto.
     *
     * @param NYTApiResponse The API response to map.
     * @return A mapped BookResponseDto.
     */
    private Set<BookDto> mapToBookResponseDto(NYTApiResponse NYTApiResponse) {
        Set<BookDto> books = new HashSet<>();
        if (NYTApiResponse != null && NYTApiResponse.getResults() != null) {
            for (NYTApiResponse.BookResult bookResult : NYTApiResponse.getResults()) {
                BookDto book = new BookDto();
                book.setName(bookResult.getTitle());
                book.setPublisher(bookResult.getPublisher());
                book.setYear(getPublishYear(bookResult));
                book.setAuthor(bookResult.getAuthor());
                books.add(book);
            }
        }
        return books;
    }

    /**
     * Set the publication year for a book using BookPublicationService.
     *
     * @param book The book to set the publication year for.
     */
    private int getPublishYear(NYTApiResponse.BookResult book) {

        Set<String> uniqueIsbnList = new HashSet<>();
        int publishYear = UNKNOWN_YEAR;

        // Collect ISBN-13 values from BookResult's Isbn objects
        List<NYTApiResponse.BookResult.Isbn> bookIsbnList = book.getIsbns();
        if (bookIsbnList != null) {
            uniqueIsbnList.addAll(bookIsbnList.stream()
                    .map(NYTApiResponse.BookResult.Isbn::getIsbn13)
                    .collect(Collectors.toList()));
        }

        // Collect ISBN-13 values from RankHistory's primary_isbn13
        List<NYTApiResponse.BookResult.RankHistory> rankHistoryList = book.getRanks_history();
        if (rankHistoryList != null) {
            uniqueIsbnList.addAll(rankHistoryList.stream()
                    .map(NYTApiResponse.BookResult.RankHistory::getPrimary_isbn13)
                    .collect(Collectors.toList()));
        }

        if (!uniqueIsbnList.isEmpty()) {
            publishYear = bookPublicationService.getPublishYearByIsbn(uniqueIsbnList);
        }
        return publishYear;
    }

    /**
     * Builds a URI for querying the New York Times API with the specified author name and offset.
     *
     * @param sanitizedAuthorName The sanitized and encoded author name to use in the query.
     * @param offset The offset parameter for pagination.
     * @return A URI object representing the constructed API URI.
     */
    private URI buildApiUri(String sanitizedAuthorName, int offset) {
        return UriComponentsBuilder.newInstance()
                .scheme(nytProperties.getScheme())
                .host(nytProperties.getHost())
                .path(nytProperties.getPath())
                .queryParam(AUTHOR, sanitizedAuthorName)
                .queryParam(OFFSET, offset)
                .queryParam(API_KEY, nytProperties.getKey())
                .build().toUri();
    }

    /**
     * Validates the provided author name.
     *
     * @param authorName The author name to be validated.
     * @throws IllegalArgumentException If the author name is null or empty after trimming.
     */
    private void validateAuthorName(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            throw new IllegalArgumentException(AUTHOR_NAME_CANNOT_BE_EMPTY);
        }
    }

    /**
     * Sanitizes and encodes the input string.
     *
     * @param input The input string to be sanitized and encoded.
     * @return The sanitized and encoded output string.
     */
    private String sanitizeAndEncode(String input) {
        input = input.trim();
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }

}
