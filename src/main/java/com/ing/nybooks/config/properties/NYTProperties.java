package com.ing.nybooks.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the New York Times (NYT) API.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "nytimes.api")
public class NYTProperties {

    /**
     * The base Scheme of the New York Times (NYT) API.
     */
    private String scheme;
    /**
     * The base Host of the New York Times (NYT) API.
     */
    private String host;
    /**
     * The base Path of the New York Times (NYT) API.
     */
    private String path;
    /**
     * The API key required for authentication with the New York Times (NYT) API.
     */
    private String key;
}
