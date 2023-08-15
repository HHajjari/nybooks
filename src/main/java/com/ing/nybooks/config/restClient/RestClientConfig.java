package com.ing.nybooks.config.restClient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration class for defining the RestClient bean based on the specified property.
 */
@Configuration
public class RestClientConfig {

    /**
     * Creates a RestClient bean using RestTemplateClient when the property "rest.client" is set to "restTemplate"
     * or not provided (default).
     *
     * @return An instance of the RestClient.
     */
    @Bean
    @ConditionalOnProperty(name = "rest.client", havingValue = "restTemplate", matchIfMissing = true)
    public RestClient restTemplateClient() {
        return new RestTemplateClient(
                new RestTemplateBuilder()
                        .setConnectTimeout(Duration.ofSeconds(5))
                        .setReadTimeout(Duration.ofSeconds(5))
                        .build());
    }
}
