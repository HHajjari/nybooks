package com.ing.nybooks.config.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for configuring different exception handling strategies based on properties.
 */
@Configuration
public class ExceptionHandlingConfig {

    /**
     * Creates an instance of LimitedExceptionHandlingStrategy as a bean, used when the property
     * "exception-handling-strategy" is set to "limited" or not provided.
     *
     * @return An instance of LimitedExceptionHandlingStrategy.
     */
    @Bean
    @ConditionalOnProperty(name = "exception-handling-strategy", havingValue = "limited", matchIfMissing = true)
    public ExceptionHandlingStrategy limitedExceptionHandlingStrategy() {
        return new LimitedExceptionHandlingStrategy();
    }

    /**
     * Creates an instance of DetailedExceptionHandlingStrategy as a bean, used when the property
     * "exception-handling-strategy" is set to "detailed".
     *
     * @return An instance of DetailedExceptionHandlingStrategy.
     */
    @Bean
    @ConditionalOnProperty(name = "exception-handling-strategy", havingValue = "detailed")
    public ExceptionHandlingStrategy detailedExceptionHandlingStrategy() {
        return new DetailedExceptionHandlingStrategy();
    }
}
