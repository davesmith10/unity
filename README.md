# Unity Markup Language

Unity is a markup language implemented in standard JSON that provides essential components from the XML Infoset. It uses JSON Arrays as the primary structural element to represent XML-like hierarchical data.

## Overview

This repository contains a reference implementation for parsing and validating Unity-encoded files.

## Unity Syntax

Unity maps JSON structures to XML-like elements:

```
["x"]                                    -> <x/>
["x", ""]                                -> <x></x>
["x", "hello world"]                     -> <x>hello world</x>
["x", {"a": "attrib1", "b": 42}]         -> <x a="attrib1" b="42"/>
["x", ["y"]]                             -> <x><y/></x>
["x", "text", ["y"], "more"]             -> <x>text<y/>more</x>
```

### Rules

1. Top level must be a JSON Array
2. Index 0: Element name (string, must conform to XML name rules)
3. Index 1: If a JSON Object, treated as attributes (primitive values only)
4. Index 2+: Content (strings, numbers, booleans, null, or nested Unity arrays)

## Requirements

- Java 21
- Maven

## Build

```bash
mvn compile
```

## Test

```bash
mvn test
```

## Usage

```java
import com.metamadbooks.unity.UnityValidator;
import com.metamadbooks.unity.ValidationResult;

UnityValidator validator = new UnityValidator();
ValidationResult result = validator.validate("[\"element\", {\"id\": \"1\"}, \"content\"]");

if (result.isValid()) {
    System.out.println("Valid Unity markup");
} else {
    result.getErrors().forEach(System.out::println);
}
```

## Example

```json
["breakfast_menu",
  ["food", {"id": "001"},
    ["name", "Belgian Waffles"],
    ["price", "$5.95"],
    ["calories", "650"]
  ],
  ["food", {"id": "002"},
    ["name", "French Toast"],
    ["price", "$4.50"],
    ["calories", "600"]
  ]
]
```

## License

Apache 2.0