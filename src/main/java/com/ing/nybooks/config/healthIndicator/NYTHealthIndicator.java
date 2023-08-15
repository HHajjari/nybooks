package com.ing.nybooks.config.healthIndicator;

import com.ing.nybooks.config.properties.NYTProperties;
import com.ing.nybooks.config.restClient.RestClient;
import com.ing.nybooks.model.external.NYTApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import static com.ing.nybooks.model.Const.*;

/**
 * Custom health indicator for checking the availability of the NYTimes API.
 */
@AllArgsConstructor
@Component
public class NYTHealthIndicator implements HealthIndicator {

    private final NYTProperties nytProperties;
    private final RestClient restClient;
    private static final Logger logger = LoggerFactory.getLogger(NYTHealthIndicator.class);

    /**
     * Checks the availability of the NYTimes API.
     *
     * @return The health status of the NYTimes API.
     */
    @Override
    public Health health() {
        // Check if the NYTimes API is available
        boolean isApiAvailable = checkNYTApiAvailability();
        if (isApiAvailable) {
            logger.info("NYTimes API is available");

            return Health.up().withDetail("message", "NYTimes API is available").build();
        } else {
            logger.warn("NYTimes API is not available");

            return Health.down().withDetail("message", "NYTimes API is not available").build();
        }
    }

    /**
     * Checks if the NYTimes API is available by making a test request.
     *
     * @return True if the API is available, false otherwise.
     */
    private boolean checkNYTApiAvailability() {
        try {
            String apiUri = buildApiUri();
            NYTApiResponse nytApiResponse = restClient.getForObject(apiUri, NYTApiResponse.class);
            return nytApiResponse != null;
        } catch (RestClientException e) {
            logger.error("Error while checking NYTimes API availability", e);

            return false;
        }
    }

    /**
     * Builds the URI for testing NYTimes API request.
     *
     * @return The constructed API URI.
     */
    private String buildApiUri() {
        return UriComponentsBuilder.newInstance()
                .scheme(nytProperties.getScheme())
                .host(nytProperties.getHost())
                .path(nytProperties.getPath())
                .queryParam(AUTHOR, "James Carville")
                .queryParam(API_KEY, nytProperties.getKey())
                .build().toUriString();
    }
}
