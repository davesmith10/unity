package com.metamadbooks.unity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XmlNameValidatorTest {

    @Test
    @DisplayName("Valid simple names are accepted")
    void validSimpleNames() {
        assertTrue(XmlNameValidator.isValidName("element"));
        assertTrue(XmlNameValidator.isValidName("Element"));
        assertTrue(XmlNameValidator.isValidName("ELEMENT"));
        assertTrue(XmlNameValidator.isValidName("x"));
        assertTrue(XmlNameValidator.isValidName("X"));
    }

    @Test
    @DisplayName("Names starting with underscore are valid")
    void namesStartingWithUnderscore() {
        assertTrue(XmlNameValidator.isValidName("_element"));
        assertTrue(XmlNameValidator.isValidName("_"));
        assertTrue(XmlNameValidator.isValidName("__double"));
    }

    @Test
    @DisplayName("Names starting with colon are valid")
    void namesStartingWithColon() {
        assertTrue(XmlNameValidator.isValidName(":element"));
    }

    @Test
    @DisplayName("Names with digits (not at start) are valid")
    void namesWithDigits() {
        assertTrue(XmlNameValidator.isValidName("element1"));
        assertTrue(XmlNameValidator.isValidName("element123"));
        assertTrue(XmlNameValidator.isValidName("e1e2e3"));
    }

    @Test
    @DisplayName("Names with hyphens are valid")
    void namesWithHyphens() {
        assertTrue(XmlNameValidator.isValidName("my-element"));
        assertTrue(XmlNameValidator.isValidName("a-b-c"));
    }

    @Test
    @DisplayName("Names with periods are valid")
    void namesWithPeriods() {
        assertTrue(XmlNameValidator.isValidName("my.element"));
        assertTrue(XmlNameValidator.isValidName("a.b.c"));
    }

    @Test
    @DisplayName("Namespaced names are valid")
    void namespacedNames() {
        assertTrue(XmlNameValidator.isValidName("ns:element"));
        assertTrue(XmlNameValidator.isValidName("prefix:localname"));
        assertTrue(XmlNameValidator.isValidName("xml:lang"));
    }

    @Test
    @DisplayName("Unicode names are valid")
    void unicodeNames() {
        assertTrue(XmlNameValidator.isValidName("élément"));
        assertTrue(XmlNameValidator.isValidName("元素"));
        assertTrue(XmlNameValidator.isValidName("στοιχείο"));
    }

    @Test
    @DisplayName("Null is invalid")
    void nullIsInvalid() {
        assertFalse(XmlNameValidator.isValidName(null));
    }

    @Test
    @DisplayName("Empty string is invalid")
    void emptyStringIsInvalid() {
        assertFalse(XmlNameValidator.isValidName(""));
    }

    @Test
    @DisplayName("Names starting with digit are invalid")
    void namesStartingWithDigitAreInvalid() {
        assertFalse(XmlNameValidator.isValidName("1element"));
        assertFalse(XmlNameValidator.isValidName("123"));
        assertFalse(XmlNameValidator.isValidName("0"));
    }

    @Test
    @DisplayName("Names starting with hyphen are invalid")
    void namesStartingWithHyphenAreInvalid() {
        assertFalse(XmlNameValidator.isValidName("-element"));
        assertFalse(XmlNameValidator.isValidName("-"));
    }

    @Test
    @DisplayName("Names starting with period are invalid")
    void namesStartingWithPeriodAreInvalid() {
        assertFalse(XmlNameValidator.isValidName(".element"));
        assertFalse(XmlNameValidator.isValidName("."));
    }

    @Test
    @DisplayName("Names with spaces are invalid")
    void namesWithSpacesAreInvalid() {
        assertFalse(XmlNameValidator.isValidName("element name"));
        assertFalse(XmlNameValidator.isValidName(" element"));
        assertFalse(XmlNameValidator.isValidName("element "));
    }

    @Test
    @DisplayName("Names with special characters are invalid")
    void namesWithSpecialCharactersAreInvalid() {
        assertFalse(XmlNameValidator.isValidName("element@name"));
        assertFalse(XmlNameValidator.isValidName("element#name"));
        assertFalse(XmlNameValidator.isValidName("element$name"));
        assertFalse(XmlNameValidator.isValidName("element%name"));
        assertFalse(XmlNameValidator.isValidName("element&name"));
        assertFalse(XmlNameValidator.isValidName("element*name"));
        assertFalse(XmlNameValidator.isValidName("element+name"));
        assertFalse(XmlNameValidator.isValidName("element=name"));
        assertFalse(XmlNameValidator.isValidName("element/name"));
        assertFalse(XmlNameValidator.isValidName("element\\name"));
    }
}
