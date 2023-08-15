package com.ing.nybooks.config.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import javax.validation.UnexpectedTypeException;

/**
 * Exception handling strategy that provides specific error responses based on exception types.
 */
@ConditionalOnProperty(name = "exception-handling-strategy", havingValue = "limited")
public class LimitedExceptionHandlingStrategy implements ExceptionHandlingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(LimitedExceptionHandlingStrategy.class);
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleNotFound(HttpClientErrorException.NotFound ex) {
        logger.error("Resource not found", ex);

        ErrorResponse errorResponse = new ErrorResponse("Resource not found: ",null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<ErrorResponse> handleApiError(RuntimeException ex) {
        logger.error("API error", ex);

        ErrorResponse errorResponse = new ErrorResponse("API error: ", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException ex) {
        logger.error("Error while making REST request", ex);

        ErrorResponse errorResponse = new ErrorResponse("Error while making REST request: ", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ErrorResponse> handleMessageConversionException(HttpMessageConversionException ex) {
        logger.error("Error while converting API response", ex);

        ErrorResponse errorResponse = new ErrorResponse("Error while converting API response: ", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(BindException ex) {
        logger.error("Validation error", ex);

        ErrorResponse errorResponse = new ErrorResponse("Validation error", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Validation error", ex);

        ErrorResponse errorResponse = new ErrorResponse("Validation error", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleIBindException(BindException ex) {
        logger.error("Validation error", ex);

        ErrorResponse errorResponse = new ErrorResponse("Validation error", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        logger.error("An unexpected error occurred", ex);

        ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
