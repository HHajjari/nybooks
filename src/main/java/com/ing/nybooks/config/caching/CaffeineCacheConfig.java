package com.ing.nybooks.config.caching;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.CacheManager;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Configuration class for setting up Caffeine-based caching.
 */
@EnableCaching // Enables Spring's caching capabilities
@Configuration
@EnableConfigurationProperties(value = CaffeineCacheDto.class)
public class CaffeineCacheConfig {
    private final CaffeineCacheDto caffeineCacheDto;

    // Constructor-based dependency injection for CaffeineCacheDto
    public CaffeineCacheConfig(CaffeineCacheDto caffeineCacheDto) {
        this.caffeineCacheDto = caffeineCacheDto;
    }

    /**
     * Creates and configures the CacheManager.
     *
     * @return The configured CacheManager.
     */
    @Bean
    public CacheManager cacheManager() {
        // Retrieve the cache configurations from the properties
        List<CaffeineCacheDto.Cache> caffeines = caffeineCacheDto.getCaffeines();

        // Create CaffeineCache instances using the cache configurations
        List<CaffeineCache> caffeineCaches = caffeines.parallelStream()
                .map(this::buildCache)
                .collect(Collectors.toList());

        // Configure and return a SimpleCacheManager with the created caches
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(caffeineCaches);
        return manager;
    }

    /**
     * Builds a CaffeineCache instance with the provided cache configuration.
     *
     * @param cacheConfig The cache configuration.
     * @return The configured CaffeineCache instance.
     */
    private CaffeineCache buildCache(CaffeineCacheDto.Cache cacheConfig) {
        return new CaffeineCache(cacheConfig.getName(), Caffeine.newBuilder()
                .expireAfterWrite(cacheConfig.getExpiryInMinutes(), TimeUnit.MINUTES)
                .build());
    }
}
