package com.ing.nybooks.config.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Interface representing a strategy for handling exceptions in a RESTful manner.
 */
@ControllerAdvice
public interface ExceptionHandlingStrategy {

    /**
     * Handles an exception and returns an appropriate ResponseEntity containing an error response.
     *
     * @param ex The exception to be handled.
     * @return ResponseEntity containing an ErrorResponse with error details.
     */
    ResponseEntity<ErrorResponse> handleException(Exception ex);
}
