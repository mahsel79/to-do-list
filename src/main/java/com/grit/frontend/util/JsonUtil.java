package com.grit.frontend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonUtil {
    private static final Logger LOGGER = Logger.getLogger(JsonUtil.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Ignore unknown properties
    }

    /**
     * Converts an object to its JSON string representation.
     *
     * @param obj the object to convert
     * @return the JSON string
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Error converting to JSON", e);
            return "{}"; // Return an empty JSON object
        }
    }

    /**
     * Converts a JSON string to an object of the specified class.
     *
     * @param json  the JSON string
     * @param clazz the class type
     * @param <T>   the type of the object
     * @return the deserialized object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Error converting from JSON", e);
            return null;
        }
    }

    /**
     * Converts a JSON string to an object of the specified type reference.
     *
     * @param json          the JSON string
     * @param typeReference the type reference
     * @param <T>           the type of the object
     * @return the deserialized object
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Error converting from JSON with TypeReference", e);
            return null;
        }
    }

    /**
     * Checks if a given string is valid JSON.
     *
     * @param json the JSON string to validate
     * @return true if valid JSON, false otherwise
     */
    public static boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.WARNING, "Invalid JSON string", e);
            return false;
        }
    }
}
