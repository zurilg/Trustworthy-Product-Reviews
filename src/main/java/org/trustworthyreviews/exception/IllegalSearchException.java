package org.trustworthyreviews.exception;

/**
 * Custom exception thrown when an illegal search operation is attempted.
 *
 * @version 11-17-2025
 */
public class IllegalSearchException extends IllegalArgumentException {
    /**
     * The constructor for IllegalSearchException.
     *
     * @param message The exception message
     */
    public IllegalSearchException(String message) {
        super(message);
    }
}
