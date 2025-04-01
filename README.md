# Generalized Number.parseNumber(String) Method for Java

## Motivation

Java provides multiple number types (byte, short, int, long, etc.), and developers typically choose them based on memory considerations. Currently, Java offers String to Number conversions using concrete classes:

1. Long.parseLong(String)
2. Integer.parseInt(String)
3. Short.parseShort(String), etc.

While these are useful, Java lacks a generalized method that returns the most memory-efficient Number representation based on the input, like:

`Number.parseNumber(String numberAsText);`

Lets consider sample use-case

`When developing **REST APIs or Data Engineering applications**, we often deal with **untyped JSON documents**, which are loaded into Java as `Map<String, Object>` (for JSON objects) or `List<Object>` (for JSON arrays). Typically, JSON parsers convert these into Java objects.`

Consider this example:

```java
Map<String, Number> originalData = Map.of(
    "byteValue", (byte) 123,
    "shortValue", (short) 1234
);
```

This would be represented as the following JSON:

```json
{
  "shortValue": 1234,
  "byteValue": 123
}
```

Now, when we **deserialize** this JSON using a parser like Jackson:

```java
Map<String, Object> deserializedData = new JsonReader().getJacksonMap(jsonString);
```

The expectation is that numeric values should be preserved in their smallest possible type (`byte` or `short`). However, **Jackson (and most JSON parsers) default to `Integer` for whole numbers** because they rely on `Integer.parseInt(String)`, which is a safe fallback for most cases.

### Why `Number.parseNumber(String)`?

If we had a **generalized `Number.parseNumber(String)` method**, it could intelligently determine the most memory-efficient number type based on the value range. This would help reduce unnecessary memory usage, especially when handling large datasets in **high-performance applications**.

### Reference Implementation

Here’s a rough implementation to illustrate the idea:

```java
private static Number parseNumber(final String numberStr) {
    try {
        if (numberStr.contains(".")) {
            double doubleValue = Double.parseDouble(numberStr);
            return (doubleValue >= -Float.MAX_VALUE && doubleValue <= Float.MAX_VALUE) ? 
                   (float) doubleValue : doubleValue;
        } else {
            long longValue = Long.parseLong(numberStr);
            if (longValue >= Byte.MIN_VALUE && longValue <= Byte.MAX_VALUE) {
                return (byte) longValue;
            } else if (longValue >= Short.MIN_VALUE && longValue <= Short.MAX_VALUE) {
                return (short) longValue;
            } else if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                return (int) longValue;
            } else {
                return longValue;
            }
        }
    } catch (NumberFormatException e) {
        return parseBigNumber(numberStr);
    }
}

private static Number parseBigNumber(final String numberStr) {
    try {
        return new BigInteger(numberStr); // Try BigInteger first
    } catch (NumberFormatException e) {
        // Only create BigDecimal if BigInteger fails
        BigDecimal bd = new BigDecimal(numberStr);
        try {
            // Convert to BigInteger if there's no fraction
            return bd.toBigIntegerExact();
        } catch (ArithmeticException ex) {
            return bd; // If it's a decimal, return BigDecimal
        }
    }
}
```

Would love to hear your [thoughts on this approach](https://mail.openjdk.org/pipermail/core-libs-dev/2025-March/141912.html)!
