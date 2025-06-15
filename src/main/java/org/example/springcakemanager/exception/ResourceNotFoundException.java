package org.example.springcakemanager.exception;

import java.io.Serial;

/**
 * Exception thrown when a specific resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param msg the detail message
     */
    public ResourceNotFoundException(String msg) {
        super(msg);
    }

}
