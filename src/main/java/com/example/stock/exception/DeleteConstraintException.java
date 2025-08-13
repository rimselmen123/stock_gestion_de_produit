package com.example.stock.exception;

/**
 * Exception thrown when trying to delete a resource that is referenced by other entities.
 */
public class DeleteConstraintException extends RuntimeException {
    
    private final String resourceType;
    private final String resourceId;
    
    public DeleteConstraintException(String resourceType, String resourceId, String reason) {
        super(String.format("Cannot delete %s with id %s: %s", resourceType, resourceId, reason));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public String getResourceId() {
        return resourceId;
    }
}