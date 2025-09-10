package com.example.stock.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 * Used for uniqueness constraint violations.
 * 
 * @author Generated
 * @since 1.0
 */
public class DuplicateResourceException extends RuntimeException {
    
    private final String resourceType;
    private final String fieldName;
    private final String fieldValue;
    
    public DuplicateResourceException(String message) {
        super(message);
        this.resourceType = null;
        this.fieldName = null;
        this.fieldValue = null;
    }
    
    public DuplicateResourceException(String resourceType, String fieldName, String fieldValue) {
        super(String.format("%s already exists with %s: %s", resourceType, fieldName, fieldValue));
        this.resourceType = resourceType;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public String getFieldValue() {
        return fieldValue;
    }
}
