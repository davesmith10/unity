package com.metamadbooks.unity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Validates Unity markup according to the Unity specification.
 * <p>
 * Unity is a markup language implemented in standard JSON that provides
 * essential components from the XML Infoset. It uses JSON Arrays as the
 * primary structural element to represent XML-like hierarchical data.
 */
public class UnityValidator {

    /**
     * Validates a Unity markup string.
     *
     * @param json the JSON string to validate as Unity markup
     * @return ValidationResult containing any errors found
     */
    public ValidationResult validate(String json) {
        ValidationResult result = new ValidationResult();

        if (json == null || json.isBlank()) {
            result.addError("", "Input is null or empty");
            return result;
        }

        Object parsed;
        try {
            parsed = parseJson(json);
        } catch (JSONException e) {
            result.addError("", "Invalid JSON: " + e.getMessage());
            return result;
        }

        if (!(parsed instanceof JSONArray)) {
            result.addError("", "Top level must be a JSON Array, got " + getTypeName(parsed));
            return result;
        }

        validateProduction((JSONArray) parsed, "", result);
        return result;
    }

    /**
     * Validates a Unity production (a JSON Array representing an element).
     *
     * @param array  the JSON array to validate
     * @param path   the current path for error reporting
     * @param result the validation result to add errors to
     */
    private void validateProduction(JSONArray array, String path, ValidationResult result) {
        if (array.isEmpty()) {
            result.addError(path, "Unity production must have at least one element (the element name)");
            return;
        }

        // Index 0: Element name (required, must be a string conforming to XML name rules)
        Object elementName = array.get(0);
        if (!(elementName instanceof String)) {
            result.addError(path + "[0]", "Element name must be a string, got " + getTypeName(elementName));
        } else {
            String name = (String) elementName;
            if (!XmlNameValidator.isValidName(name)) {
                result.addError(path + "[0]", "Invalid XML element name: \"" + name + "\"");
            }
        }

        if (array.length() == 1) {
            return; // Valid: just element name, self-closing
        }

        // Determine where content starts based on whether index 1 is an attributes object
        int contentStart = 1;
        Object second = array.get(1);

        if (second instanceof JSONObject) {
            // Index 1 is attributes
            validateAttributes((JSONObject) second, path + "[1]", result);
            contentStart = 2;
        }

        // Validate remaining content items
        for (int i = contentStart; i < array.length(); i++) {
            validateContent(array.get(i), path + "[" + i + "]", result);
        }
    }

    /**
     * Validates an attributes object.
     * Attribute names must conform to XML name rules.
     * Attribute values must be primitives (String, Number, Boolean, or Null).
     */
    private void validateAttributes(JSONObject attrs, String path, ValidationResult result) {
        for (String key : attrs.keySet()) {
            if (!XmlNameValidator.isValidName(key)) {
                result.addError(path + "." + key, "Invalid XML attribute name: \"" + key + "\"");
            }

            Object value = attrs.get(key);
            if (!isPrimitive(value)) {
                result.addError(path + "." + key,
                        "Attribute value must be a primitive (String, Number, Boolean, or Null), got " + getTypeName(value));
            }
        }
    }

    /**
     * Validates a content item.
     * Content may be a primitive or a nested Unity production (JSONArray).
     */
    private void validateContent(Object content, String path, ValidationResult result) {
        if (content instanceof JSONArray) {
            // Nested Unity production - validate recursively
            validateProduction((JSONArray) content, path, result);
        } else if (content instanceof JSONObject) {
            // JSON Objects are only allowed at index 1 as attributes
            result.addError(path, "JSON Object not allowed as content (only allowed at index 1 as attributes)");
        } else if (!isPrimitive(content)) {
            result.addError(path, "Invalid content type: " + getTypeName(content));
        }
        // Primitives (String, Number, Boolean, Null) are valid content
    }

    /**
     * Checks if a value is a JSON primitive (String, Number, Boolean, or Null).
     */
    private boolean isPrimitive(Object value) {
        return value == null
                || value == JSONObject.NULL
                || value instanceof String
                || value instanceof Number
                || value instanceof Boolean;
    }

    /**
     * Returns a human-readable type name for error messages.
     */
    private String getTypeName(Object value) {
        if (value == null || value == JSONObject.NULL) {
            return "null";
        } else if (value instanceof JSONArray) {
            return "Array";
        } else if (value instanceof JSONObject) {
            return "Object";
        } else if (value instanceof String) {
            return "String";
        } else if (value instanceof Number) {
            return "Number";
        } else if (value instanceof Boolean) {
            return "Boolean";
        } else {
            return value.getClass().getSimpleName();
        }
    }

    /**
     * Parses a JSON string, returning either a JSONArray or JSONObject.
     */
    private Object parseJson(String json) throws JSONException {
        String trimmed = json.trim();
        if (trimmed.startsWith("[")) {
            return new JSONArray(trimmed);
        } else if (trimmed.startsWith("{")) {
            return new JSONObject(trimmed);
        } else {
            throw new JSONException("JSON must start with '[' or '{'");
        }
    }
}
