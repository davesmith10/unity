package com.metamadbooks.unity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnityValidatorTest {

    private UnityValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UnityValidator();
    }

    @Nested
    @DisplayName("Basic element tests")
    class BasicElementTests {

        @Test
        @DisplayName("[\"x\"] is valid (self-closing element)")
        void selfClosingElement() {
            ValidationResult result = validator.validate("[\"x\"]");
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("[\"x\", \"\"] is valid (empty element with tags)")
        void emptyElementWithTags() {
            ValidationResult result = validator.validate("[\"x\", \"\"]");
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("[\"x\", \"hello world\"] is valid (element with text content)")
        void elementWithTextContent() {
            ValidationResult result = validator.validate("[\"x\", \"hello world\"]");
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("Element with numeric content is valid")
        void elementWithNumericContent() {
            ValidationResult result = validator.validate("[\"x\", 42]");
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("Element with boolean content is valid")
        void elementWithBooleanContent() {
            ValidationResult result = validator.validate("[\"x\", true]");
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("Element with null content is valid")
        void elementWithNullContent() {
            ValidationResult result = validator.validate("[\"x\", null]");
            assertTrue(result.isValid(), result.toString());
        }
    }

    @Nested
    @DisplayName("Attribute tests")
    class AttributeTests {

        @Test
        @DisplayName("Element with attributes is valid")
        void elementWithAttributes() {
            ValidationResult result = validator.validate("[\"x\", {\"a\": \"attrib1\", \"b\": 42}]");
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("Element with attributes and content is valid")
        void elementWithAttributesAndContent() {
            ValidationResult result = validator.validate("[\"x\", {\"a\": \"attrib1\"}, \"content\"]");
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("Attributes with boolean values are valid")
        void attributesWithBooleanValues() {
            ValidationResult result = validator.validate("[\"x\", {\"enabled\": true, \"visible\": false}]");
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("Attributes with null values are valid")
        void attributesWithNullValues() {
            ValidationResult result = validator.validate("[\"x\", {\"value\": null}]");
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("Nested object in attributes is invalid")
        void nestedObjectInAttributesIsInvalid() {
            ValidationResult result = validator.validate("[\"x\", {\"a\": {\"nested\": true}}]");
            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream()
                    .anyMatch(e -> e.message().contains("primitive")));
        }

        @Test
        @DisplayName("Array in attributes is invalid")
        void arrayInAttributesIsInvalid() {
            ValidationResult result = validator.validate("[\"x\", {\"a\": [1, 2, 3]}]");
            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream()
                    .anyMatch(e -> e.message().contains("primitive")));
        }
    }

    @Nested
    @DisplayName("Nested element tests")
    class NestedElementTests {

        @Test
        @DisplayName("[\"x\", [\"y\"]] is valid (nested element)")
        void nestedElement() {
            ValidationResult result = validator.validate("[\"x\", [\"y\"]]");
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("Multiple nested elements are valid")
        void multipleNestedElements() {
            ValidationResult result = validator.validate("[\"x\", [\"y\"], [\"z\"]]");
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("Mixed content (text and elements) is valid")
        void mixedContent() {
            ValidationResult result = validator.validate("[\"x\", \"text before\", [\"y\"], \"text after\"]");
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("Deeply nested elements are valid")
        void deeplyNestedElements() {
            ValidationResult result = validator.validate("[\"a\", [\"b\", [\"c\", [\"d\", \"deep\"]]]]");
            assertTrue(result.isValid(), result.toString());
        }
    }

    @Nested
    @DisplayName("Complex structure tests")
    class ComplexStructureTests {

        @Test
        @DisplayName("Breakfast menu example is valid")
        void breakfastMenuExample() {
            String json = """
                ["breakfast_menu",
                  ["food", {"id": "000001"},
                    ["name", "Belgian Waffles"],
                    ["price", "$5.95"],
                    ["description", "Two of our famous Belgian Waffles"],
                    ["calories", "650"]
                  ],
                  ["food", {"id": "000002"},
                    ["name", "French Toast"],
                    ["price", "$4.50"]
                  ]
                ]
                """;
            ValidationResult result = validator.validate(json);
            assertTrue(result.isValid(), result.toString());
        }

        @Test
        @DisplayName("Element with attributes and multiple nested children is valid")
        void elementWithAttributesAndChildren() {
            String json = """
                ["parent", {"id": "1", "class": "container"},
                  ["child", {"name": "first"}, "text1"],
                  ["child", {"name": "second"}, "text2"]
                ]
                """;
            ValidationResult result = validator.validate(json);
            assertTrue(result.isValid(), result.toString());
        }
    }

    @Nested
    @DisplayName("Invalid input tests")
    class InvalidInputTests {

        @Test
        @DisplayName("Null input is invalid")
        void nullInput() {
            ValidationResult result = validator.validate(null);
            assertFalse(result.isValid());
        }

        @Test
        @DisplayName("Empty string is invalid")
        void emptyString() {
            ValidationResult result = validator.validate("");
            assertFalse(result.isValid());
        }

        @Test
        @DisplayName("Invalid JSON is invalid")
        void invalidJson() {
            ValidationResult result = validator.validate("not json");
            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream()
                    .anyMatch(e -> e.message().contains("Invalid JSON")));
        }

        @Test
        @DisplayName("JSON Object at top level is invalid")
        void objectAtTopLevel() {
            ValidationResult result = validator.validate("{\"x\": 1}");
            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream()
                    .anyMatch(e -> e.message().contains("Top level must be a JSON Array")));
        }

        @Test
        @DisplayName("Empty array is invalid")
        void emptyArray() {
            ValidationResult result = validator.validate("[]");
            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream()
                    .anyMatch(e -> e.message().contains("must have at least one element")));
        }

        @Test
        @DisplayName("Non-string element name is invalid")
        void nonStringElementName() {
            ValidationResult result = validator.validate("[123]");
            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream()
                    .anyMatch(e -> e.message().contains("Element name must be a string")));
        }

        @Test
        @DisplayName("JSON Object as content (not at index 1) is invalid")
        void objectAsContent() {
            ValidationResult result = validator.validate("[\"x\", \"text\", {\"not\": \"allowed\"}]");
            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream()
                    .anyMatch(e -> e.message().contains("JSON Object not allowed as content")));
        }
    }

    @Nested
    @DisplayName("XML name validation tests")
    class XmlNameTests {

        @Test
        @DisplayName("Valid XML names are accepted")
        void validXmlNames() {
            assertTrue(validator.validate("[\"element\"]").isValid());
            assertTrue(validator.validate("[\"_element\"]").isValid());
            assertTrue(validator.validate("[\"element123\"]").isValid());
            assertTrue(validator.validate("[\"my-element\"]").isValid());
            assertTrue(validator.validate("[\"my.element\"]").isValid());
            assertTrue(validator.validate("[\"ns:element\"]").isValid());
        }

        @Test
        @DisplayName("Element name starting with digit is invalid")
        void elementNameStartingWithDigit() {
            ValidationResult result = validator.validate("[\"123element\"]");
            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream()
                    .anyMatch(e -> e.message().contains("Invalid XML element name")));
        }

        @Test
        @DisplayName("Element name with spaces is invalid")
        void elementNameWithSpaces() {
            ValidationResult result = validator.validate("[\"element name\"]");
            assertFalse(result.isValid());
        }

        @Test
        @DisplayName("Empty element name is invalid")
        void emptyElementName() {
            ValidationResult result = validator.validate("[\"\"]");
            assertFalse(result.isValid());
        }

        @Test
        @DisplayName("Invalid attribute name is detected")
        void invalidAttributeName() {
            ValidationResult result = validator.validate("[\"x\", {\"123invalid\": \"value\"}]");
            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream()
                    .anyMatch(e -> e.message().contains("Invalid XML attribute name")));
        }
    }
}
