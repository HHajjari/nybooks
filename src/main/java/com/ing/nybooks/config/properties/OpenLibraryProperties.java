package com.ing.nybooks.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the Open Library API.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "openlibrary.api")
public class OpenLibraryProperties {

    /**
     * The base URL of the Open Library API.
     */
    private String url;
}
