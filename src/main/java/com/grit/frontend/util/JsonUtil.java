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
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Error converting to JSON", e);
            return "{}";
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Error converting from JSON", e);
            return null;
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Error converting from JSON with TypeReference", e);
            return null;
        }
    }

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