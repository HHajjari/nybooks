package com.ing.nybooks.service;

import java.util.Set;

/**
 * Service interface for retrieving book publication year by ISBN.
 */
public interface BookPublicationService {
    /**
     * Retrieves the publication year of a book based on its ISBN.
     *
     * @param isbnList The ISBN List of the book.
     * @return The publication year of the book.
     */
    int getPublishYearByIsbn(Set<String> isbnList);
}
