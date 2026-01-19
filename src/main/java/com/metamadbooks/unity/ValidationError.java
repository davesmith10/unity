package com.metamadbooks.unity;

/**
 * Represents a validation error found during Unity markup validation.
 *
 * @param path    JSON path to the error location (e.g., "[0]", "[1][2]")
 * @param message Description of the validation error
 */
public record ValidationError(String path, String message) {

    @Override
    public String toString() {
        return path + ": " + message;
    }
}
