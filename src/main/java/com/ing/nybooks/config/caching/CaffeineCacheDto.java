package com.ing.nybooks.config.caching;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration properties class for defining Caffeine cache configurations.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "caches")
public class CaffeineCacheDto {
    // List of Caffeine cache configurations
    private List<Cache> caffeines;

    /**
     * Inner class representing a single Caffeine cache configuration.
     */
    @Getter
    @Setter
    public static class Cache {
        // Cache name
        private String name;
        // Expiry time for the cache in minutes
        private Long expiryInMinutes;
    }
}
