package com.example.stock.exception;

/**
 * Exception thrown when a resource operation conflicts with the current state.
 * Used for business rule violations and dependency conflicts.
 * 
 * @author Generated
 * @since 1.0
 */
public class ResourceConflictException extends RuntimeException {
    
    private final String resourceType;
    private final String resourceId;
    private final String conflictReason;
    
    public ResourceConflictException(String message) {
        super(message);
        this.resourceType = null;
        this.resourceId = null;
        this.conflictReason = null;
    }
    
    public ResourceConflictException(String resourceType, String resourceId, String conflictReason) {
        super(String.format("Cannot modify %s with id %s: %s", resourceType, resourceId, conflictReason));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.conflictReason = conflictReason;
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public String getResourceId() {
        return resourceId;
    }
    
    public String getConflictReason() {
        return conflictReason;
    }
}
