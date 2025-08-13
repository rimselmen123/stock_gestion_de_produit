package com.example.stock.exception;

/**
 * Exception thrown when a foreign key constraint is violated.
 */
public class ForeignKeyConstraintException extends RuntimeException {
    
    private final String fieldName;
    private final String fieldValue;
    
    public ForeignKeyConstraintException(String fieldName, String fieldValue) {
        super(String.format("Referenced entity does not exist for field %s with value: %s", fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public String getFieldValue() {
        return fieldValue;
    }
}