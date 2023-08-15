package com.ing.nybooks.service;

import com.ing.nybooks.model.dto.BookDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for searching and retrieving books based on author and years.
 */
@Service
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    private final NYTBookService nytBookService;

    /**
     * Constructor for SearchService.
     *
     * @param nytBookService The service for retrieving books from NYTimes API.
     */
    public SearchService(NYTBookService nytBookService) {
        this.nytBookService = nytBookService;
    }

    /**
     * Retrieve books by author.
     *
     * @param author The author's name.
     * @return A BookResponseDto containing book information.
     */
    public Set<BookDto> getBooksByAuthor(String author) {
        logger.info("[getBooksByAuthor] Getting books by author: {}", author);

        // Retrieve all books by the author from the NYTimes API
        Set<BookDto> response = nytBookService.getBooksByAuthor(author);
        logger.debug("[getBooksByAuthor] Received {} books for author: {}", response.size(), author);

        return response;
    }

    /**
     * Retrieve books by author and filter by publication years.
     *
     * @param author The author's name.
     * @param years  The list of years to filter by.
     * @return A BookResponseDto containing filtered book information.
     */
    public Set<BookDto> getBooksByAuthorAndYear(String author, List<Integer> years) {
        logger.info("[getBooksByAuthorAndYear] Getting books by author: {} and years: {}", author, years);

        // Retrieve all books by the author from the NYTimes API
        Set<BookDto> books = nytBookService.getBooksByAuthor(author);

        // Filter the books based on the specified years
        Set<BookDto> filteredBooks = books.stream()
                .filter(book -> years.contains(book.getYear()))
                .collect(Collectors.toSet());

        logger.debug("[getBooksByAuthorAndYear] Received {} books for author {} and years {}", filteredBooks.size(), author, years);

        return filteredBooks;
    }
}
