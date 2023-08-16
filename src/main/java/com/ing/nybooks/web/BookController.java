package com.ing.nybooks.web;

import com.ing.nybooks.model.dto.BookDto;
import com.ing.nybooks.model.dto.BookRequestDto;
import com.ing.nybooks.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.Set;

import static com.ing.nybooks.model.Const.ME_BOOKS_LIST;

/**
 * Controller class for handling search books.
 */
@RestController
@AllArgsConstructor
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final SearchService searchService;
    /**
     * Endpoint for searching and retrieving books based on author and year.
     *
     * @param bookRequestDto The request parameters.
     * @return A BookDto containing book information.
     */
    @GetMapping(ME_BOOKS_LIST)
    public Set<BookDto> searchBooks(@Valid BookRequestDto bookRequestDto) {
        logger.info("[searchBooks] Received a searchBooks request. Parameters: {}", bookRequestDto);

        if (bookRequestDto.getYear() == null || bookRequestDto.getYear().isEmpty()) {
            logger.debug("[searchBooks] Searching books by author: {}", bookRequestDto.getAuthor());

            return searchService.getBooksByAuthor(bookRequestDto.getAuthor());
        } else {
            logger.debug("[searchBooks] Searching books by author: {} and year: {}", bookRequestDto.getAuthor(), bookRequestDto.getYear());

            return searchService.getBooksByAuthorAndYear(bookRequestDto.getAuthor(), bookRequestDto.getYear());
        }
    }
}
