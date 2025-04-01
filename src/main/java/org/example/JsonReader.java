package org.example;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Program that gets JSON Objects as Map<String,Object>with different Parsers
 */
public class JsonReader {

    /**
     * Reads Json Object using Jackson.
     */
    public Map<String,Object> getJacksonMap(String jsonString) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper
                .readValue(new StringReader(jsonString),
                        new TypeReference<>() {});
    }


}
