package com.ing.nybooks.service;

import com.ing.nybooks.config.properties.OpenLibraryProperties;
import com.ing.nybooks.model.Const;
import com.ing.nybooks.model.external.OpenLibraryApiResponse;
import com.ing.nybooks.config.restClient.RestClient;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ing.nybooks.model.Const.UNKNOWN_YEAR;

/**
 * Service class for interacting with the OpenLibrary API to retrieve book publication year by ISBN.
 */
@Service
public class OpenLibraryService implements BookPublicationService {
    private static final Logger logger = LoggerFactory.getLogger(OpenLibraryService.class);
    private final OpenLibraryProperties openLibraryProperties;
    private final RestClient restClient;
    private final Timer timer;

    /**
     * Constructor for OpenLibraryService.
     *
     * @param openLibraryProperties Configuration properties for OpenLibrary API.
     * @param restClient            REST client for making API calls.
     * @param timer                 Timer for recording metrics.
     */
    public OpenLibraryService(OpenLibraryProperties openLibraryProperties, RestClient restClient, @Qualifier("openLibraryApiCallTimer") Timer timer) {
        this.openLibraryProperties = openLibraryProperties;
        this.restClient = restClient;
        this.timer = timer;
    }

    /**
     * Retrieve publication year by ISBN and cache the result.
     *
     * @param isbnList List of ISBN of the book.
     * @return The publication year.
     */
    @Override
    @Cacheable(cacheNames = "getPublishYearByIsbn", key = "#isbnList")
    public int getPublishYearByIsbn(Set<String> isbnList) {
        logger.info("[getPublicationYearByIsbn] Retrieving publication year for ISBN List: {}", isbnList);

        for (String isbn : isbnList) {
            try {
                int publicationYear = fetchPublicationYear(isbn);
                if (publicationYear > 0) {
                    logger.debug("[getPublicationYearByIsbn] Retrieved publication year for ISBN {}: {}", isbn, publicationYear);
                    return publicationYear; // Return the first valid publication year found
                }
            } catch (Exception e) {
                logger.warn("[getPublicationYearByIsbn] Error while retrieving publication year for ISBN {}", isbn, e);
            }
        }

        logger.info("[getPublicationYearByIsbn] No valid publication year found for ISBN List: {}", isbnList);
        return UNKNOWN_YEAR; // Return -2 if no valid publication year is found
    }
    /**
     * Fetches the publication year for a given ISBN from OpenLibrary API.
     *
     * @param isbn The ISBN (International Standard Book Number) for which to retrieve the publication year.
     * @return The publication year of the book corresponding to the provided ISBN, or -1 if an error occurs.
     */
    private int fetchPublicationYear(String isbn) {
        logger.info("[fetchPublicationYear] Retrieving publication year for ISBN: {}", isbn);

        String uri = buildApiUri(isbn);
        try {
            Timer.Sample sample = Timer.start();
            OpenLibraryApiResponse response = restClient.getForObject(uri, OpenLibraryApiResponse.class);
            timer.record(() -> sample.stop(timer) / 1000000);
            int publicationYear = extractYear(response);
            logger.info("[fetchPublicationYear] Retrieved publication year {} for ISBN: {}", publicationYear, isbn);

            return publicationYear;
        } catch (Exception e) {
            logger.warn("[fetchPublicationYear] Error while retrieving publication year for ISBN {}", isbn, e);

            return UNKNOWN_YEAR;
        }
    }
    /**
     * Extract the publication year from the API response.
     *
     * @param response The API response containing book information.
     * @return The extracted publication year.
     */
    private int extractYear(OpenLibraryApiResponse response) {
        Pattern pattern = Pattern.compile("\\b\\d{4}\\b");
        Matcher matcher = pattern.matcher(response.getPublish_date());
        if (matcher.find()) {
            String year = matcher.group();
            return Integer.parseInt(year);
        }
        logger.error("[extractYear] Year value could not be extracted from {}", response.getPublish_date());

        return UNKNOWN_YEAR;
    }

    /**
     * Build the API URI for OpenLibrary API.
     *
     * @param isbn The ISBN of the book.
     * @return The built API URI.
     */
    private String buildApiUri(String isbn) {
        return UriComponentsBuilder.fromHttpUrl(String.format(openLibraryProperties.getUrl(), isbn)).toUriString();
    }
}
