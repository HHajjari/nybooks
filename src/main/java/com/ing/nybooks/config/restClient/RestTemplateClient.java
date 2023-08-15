package com.ing.nybooks.config.restClient;

import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Implementation of the RestClient interface using Spring's RestTemplate.
 */
@AllArgsConstructor
public class RestTemplateClient implements RestClient {

    private final RestTemplate restTemplate;

    /**
     * Performs an HTTP GET request using RestTemplate to the specified URL and returns the response
     * converted to the specified response type.
     *
     * @param url          The URL to send the GET request to.
     * @param responseType The class type to which the response should be converted.
     * @param <T>          The type of the response.
     * @return The response object of the specified type.
     */
    @Override
    public <T> T getForObject(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    @Override
    public <T> T getForObject(URI url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }
}
