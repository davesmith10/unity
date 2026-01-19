package com.metamadbooks.unity;

/**
 * Validates XML element and attribute names per the XML 1.0 specification.
 * <p>
 * Reference: <a href="https://www.w3.org/TR/xml/#NT-Name">XML 1.0 Name production</a>
 */
public final class XmlNameValidator {

    private XmlNameValidator() {
    }

    /**
     * Validates that the given string is a valid XML Name.
     *
     * @param name the name to validate
     * @return true if the name is a valid XML Name, false otherwise
     */
    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        if (!isNameStartChar(name.charAt(0))) {
            return false;
        }

        for (int i = 1; i < name.length(); i++) {
            if (!isNameChar(name.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if a character is a valid XML NameStartChar.
     * <p>
     * NameStartChar ::= ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] |
     * [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] |
     * [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] |
     * [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
     */
    private static boolean isNameStartChar(char c) {
        return c == ':'
                || (c >= 'A' && c <= 'Z')
                || c == '_'
                || (c >= 'a' && c <= 'z')
                || (c >= 0xC0 && c <= 0xD6)
                || (c >= 0xD8 && c <= 0xF6)
                || (c >= 0xF8 && c <= 0x2FF)
                || (c >= 0x370 && c <= 0x37D)
                || (c >= 0x37F && c <= 0x1FFF)
                || (c >= 0x200C && c <= 0x200D)
                || (c >= 0x2070 && c <= 0x218F)
                || (c >= 0x2C00 && c <= 0x2FEF)
                || (c >= 0x3001 && c <= 0xD7FF)
                || (c >= 0xF900 && c <= 0xFDCF)
                || (c >= 0xFDF0 && c <= 0xFFFD);
        // Note: Characters >= 0x10000 require surrogate pair handling
    }

    /**
     * Checks if a character is a valid XML NameChar.
     * <p>
     * NameChar ::= NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
     */
    private static boolean isNameChar(char c) {
        return isNameStartChar(c)
                || c == '-'
                || c == '.'
                || (c >= '0' && c <= '9')
                || c == 0xB7
                || (c >= 0x0300 && c <= 0x036F)
                || (c >= 0x203F && c <= 0x2040);
    }
}
