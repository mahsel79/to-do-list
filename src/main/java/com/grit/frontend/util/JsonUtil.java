package com.grit.frontend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print
    }

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // Handle the exception (e.g., log it)
            System.err.println("Error converting to JSON: " + e.getMessage());
            return "{}"; // Return an empty JSON object or handle as needed
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            // Handle the exception (e.g., log it)
            System.err.println("Error converting from JSON: " + e.getMessage());
            return null; // Return null or handle as needed
        }
    }
}