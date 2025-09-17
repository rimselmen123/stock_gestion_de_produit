package com.example.stock.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Professional JSON utility class for serialization and deserialization operations.
 * Provides robust error handling and logging for JSON operations.
 * 
 * @author Development Team
 * @since 1.0
 */
@Component
@Slf4j
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Private constructor to prevent instantiation
    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    static {
        try {
            // Configure ObjectMapper for robust JSON handling
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            
            log.info("JsonUtils ObjectMapper configured successfully");
        } catch (Exception e) {
            log.error("Failed to configure JsonUtils ObjectMapper: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize JsonUtils", e);
        }
    }

    /**
     * Convert object to JSON string with comprehensive error handling.
     * 
     * @param object Object to convert to JSON
     * @return JSON string representation, null if object is null or conversion fails
     */
    public static String toJson(Object object) {
        if (object == null) {
            log.debug("Attempted to convert null object to JSON - returning null");
            return null;
        }

        try {
            String json = objectMapper.writeValueAsString(object);
            log.debug("Successfully converted object of type {} to JSON (length: {})", 
                     object.getClass().getSimpleName(), json.length());
            return json;
            
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object of type {} to JSON: {}", 
                     object.getClass().getSimpleName(), e.getMessage(), e);
            return null;
            
        } catch (Exception e) {
            log.error("Unexpected error converting object of type {} to JSON: {}", 
                     object.getClass().getSimpleName(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * Convert JSON string to object with comprehensive error handling.
     * 
     * @param <T> Target type
     * @param json JSON string to convert
     * @param clazz Target class
     * @return Converted object, null if JSON is null/empty or conversion fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            log.debug("Attempted to convert null/empty JSON to object of type {} - returning null", 
                     clazz.getSimpleName());
            return null;
        }

        if (clazz == null) {
            log.error("Target class is null - cannot convert JSON");
            return null;
        }

        try {
            T result = objectMapper.readValue(json, clazz);
            log.debug("Successfully converted JSON (length: {}) to object of type {}", 
                     json.length(), clazz.getSimpleName());
            return result;
            
        } catch (JsonProcessingException e) {
            log.error("Failed to convert JSON to object of type {}: {}. JSON content: {}", 
                     clazz.getSimpleName(), e.getMessage(), 
                     json.length() > 200 ? json.substring(0, 200) + "..." : json, e);
            return null;
            
        } catch (Exception e) {
            log.error("Unexpected error converting JSON to object of type {}: {}. JSON content: {}", 
                     clazz.getSimpleName(), e.getMessage(), 
                     json.length() > 200 ? json.substring(0, 200) + "..." : json, e);
            return null;
        }
    }

    /**
     * Convert object to JSON string with exception thrown on failure.
     * Use this method when you need to handle JSON conversion errors explicitly.
     * 
     * @param object Object to convert
     * @return JSON string
     * @throws JsonProcessingException if conversion fails
     * @throws IllegalArgumentException if object is null
     */
    public static String toJsonStrict(Object object) throws JsonProcessingException {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null for strict JSON conversion");
        }

        try {
            String json = objectMapper.writeValueAsString(object);
            log.debug("Strict conversion: Successfully converted object of type {} to JSON", 
                     object.getClass().getSimpleName());
            return json;
            
        } catch (JsonProcessingException e) {
            log.error("Strict conversion failed for object of type {}: {}", 
                     object.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    /**
     * Convert JSON string to object with exception thrown on failure.
     * Use this method when you need to handle JSON conversion errors explicitly.
     * 
     * @param <T> Target type
     * @param json JSON string
     * @param clazz Target class
     * @return Converted object
     * @throws JsonProcessingException if conversion fails
     * @throws IllegalArgumentException if parameters are invalid
     */
    public static <T> T fromJsonStrict(String json, Class<T> clazz) throws JsonProcessingException {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON string cannot be null or empty for strict conversion");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("Target class cannot be null for strict conversion");
        }

        try {
            T result = objectMapper.readValue(json, clazz);
            log.debug("Strict conversion: Successfully converted JSON to object of type {}", 
                     clazz.getSimpleName());
            return result;
            
        } catch (JsonProcessingException e) {
            log.error("Strict conversion failed for target type {}: {}. JSON: {}", 
                     clazz.getSimpleName(), e.getMessage(), 
                     json.length() > 100 ? json.substring(0, 100) + "..." : json);
            throw e;
        }
    }

    /**
     * Validate if a string is valid JSON.
     * 
     * @param json String to validate
     * @return true if valid JSON, false otherwise
     */
    public static boolean isValidJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return false;
        }

        try {
            objectMapper.readTree(json);
            return true;
        } catch (Exception e) {
            log.debug("Invalid JSON detected: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Pretty print JSON string for debugging purposes.
     * 
     * @param json JSON string to format
     * @return Formatted JSON string, original string if formatting fails
     */
    public static String prettyPrint(String json) {
        if (json == null || json.trim().isEmpty()) {
            return json;
        }

        try {
            Object jsonObject = objectMapper.readValue(json, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            log.debug("Failed to pretty print JSON: {}", e.getMessage());
            return json; // Return original if formatting fails
        }
    }
}