package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

class JsonReaderTest {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    /**
     * We create smaller numbers using json string and observer the memory use.
     */
    @Test
    void testNumbersMemeory() throws IOException {

        Map<String, Number> originalData = Map.of(
                "byteValue", (byte) 123,
                "shortValue", (short) 1234);

        System.out.println("Original Data\n");

        originalData.forEach((s, number) -> {
            System.out.format("%20s\t\t%20s\n",
                    ANSI_RESET + s,
                    ANSI_GREEN + number + " as " + number.getClass().getSimpleName());
        });

        final String jsonString = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(originalData);

        System.out.println("\n" + ANSI_RESET + "JSON String to be parsed\n");

        System.out.println(jsonString);

        Map<String, Object> deserailzedData = new JsonReader().getJacksonMap(jsonString);

        System.out.println("\n" + ANSI_RESET + "Deserialized Data\n");

        deserailzedData.forEach((s, number) -> {
            System.out.format("%20s\t\t%20s\n",
                    ANSI_RESET + s,
                    ANSI_RED + number + " as " + number.getClass().getSimpleName());
        });

    }

}