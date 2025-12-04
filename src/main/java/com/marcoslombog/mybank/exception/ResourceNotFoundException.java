package com.marcoslombog.mybank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * PUBLIC_INTERFACE
 * ResourceNotFoundException indicates that a requested resource (e.g., Account) was not found.
 * It results in a 404 Not Found response status.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * PUBLIC_INTERFACE
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * @param message detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * PUBLIC_INTERFACE
     * Constructs a new ResourceNotFoundException with no detail message.
     */
    public ResourceNotFoundException() {
        super("Resource not found");
    }
}
