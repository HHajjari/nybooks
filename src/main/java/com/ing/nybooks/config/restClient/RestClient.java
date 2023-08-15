package com.ing.nybooks.config.restClient;

import java.net.URI;

/**
 * Interface defining methods for making REST API requests.
 */
public interface RestClient {

    /**
     * Performs an HTTP GET request to the specified URL and returns the response
     * converted to the specified response type.
     *
     * @param url          The URL to send the GET request to.
     * @param responseType The class type to which the response should be converted.
     * @param <T>          The type of the response.
     * @return The response object of the specified type.
     */
    <T> T getForObject(String url, Class<T> responseType);

    /**
     * Performs an HTTP GET request to the specified URL and returns the response
     * converted to the specified response type.
     *
     * @param uri          The URL to send the GET request to.
     * @param responseType The class type to which the response should be converted.
     * @param <T>          The type of the response.
     * @return The response object of the specified type.
     */
    <T> T getForObject(URI uri, Class<T> responseType);
}
