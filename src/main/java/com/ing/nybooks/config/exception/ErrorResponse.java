package com.ing.nybooks.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents an error response containing error details.
 */
@AllArgsConstructor
@Getter
public class ErrorResponse {

    /**
     * A short, human-readable identifier for the error.
     */
    private String error;

    /**
     * A more detailed message explaining the error.
     */
    private String message;
}
