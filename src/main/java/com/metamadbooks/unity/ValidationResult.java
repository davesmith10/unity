package com.metamadbooks.unity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the result of Unity markup validation.
 */
public class ValidationResult {

    private final List<ValidationError> errors = new ArrayList<>();

    public void addError(String path, String message) {
        errors.add(new ValidationError(path, message));
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<ValidationError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    @Override
    public String toString() {
        if (isValid()) {
            return "Valid Unity markup";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid Unity markup (").append(errors.size()).append(" error(s)):\n");
        for (ValidationError error : errors) {
            sb.append("  - ").append(error).append("\n");
        }
        return sb.toString();
    }
}
