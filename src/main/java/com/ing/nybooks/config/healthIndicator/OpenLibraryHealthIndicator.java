package com.ing.nybooks.config.healthIndicator;

import com.ing.nybooks.config.properties.OpenLibraryProperties;
import com.ing.nybooks.config.restClient.RestClient;
import com.ing.nybooks.model.external.OpenLibraryApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Custom health indicator for checking the availability of the OpenLibrary API.
 */
@AllArgsConstructor
@Component
public class OpenLibraryHealthIndicator implements HealthIndicator {

    private final OpenLibraryProperties openLibraryProperties;
    private final RestClient restClient;

    // SLF4J Logger
    private static final Logger logger = LoggerFactory.getLogger(OpenLibraryHealthIndicator.class);

    /**
     * Checks the availability of the OpenLibrary API.
     *
     * @return The health status of the OpenLibrary API.
     */
    @Override
    public Health health() {
        // Check if the OpenLibrary API is available
        boolean apiAvailable = checkApiAvailability();
        if (apiAvailable) {
            logger.info("OpenLibrary API is available");
            return Health.up().withDetail("message", "OpenLibrary API is available").build();
        } else {
            logger.warn("OpenLibrary API is not available");
            return Health.down().withDetail("message", "OpenLibrary API is not available").build();
        }
    }

    /**
     * Checks if the OpenLibrary API is available by making a test request.
     *
     * @return True if the API is available, false otherwise.
     */
    private boolean checkApiAvailability() {
        try {
            // Build the API request URI
            String uri = buildApiUri();
            // Make a request to the OpenLibrary API
            OpenLibraryApiResponse response = restClient.getForObject(uri, OpenLibraryApiResponse.class);
            // If the response is not null, the API is available
            return response != null;
        } catch (Exception e) {
            // Log the exception for debugging purposes
            logger.error("Error while checking OpenLibrary API availability", e);
            return false;
        }
    }

    /**
     * Builds the URI for the OpenLibrary API request.
     *
     * @return The constructed API URI.
     */
    private String buildApiUri() {
        // Build the URI with the necessary query parameters
        return UriComponentsBuilder.fromHttpUrl(String.format(openLibraryProperties.getUrl(), "9780684857343")).toUriString();
    }
}
