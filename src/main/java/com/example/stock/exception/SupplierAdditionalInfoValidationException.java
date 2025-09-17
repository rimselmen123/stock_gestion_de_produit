package com.example.stock.exception;

import lombok.Getter;

import java.util.List;

/**
 * Exception for supplier additional info validation errors.
 * Provides detailed validation error messages for business rules.
 * 
 * @author Development Team
 * @since 1.0
 */
@Getter
public class SupplierAdditionalInfoValidationException extends RuntimeException {

    private final List<String> validationErrors;
    private final String supplierInfo;

    /**
     * Constructor with validation errors.
     * 
     * @param validationErrors List of validation error messages
     */
    public SupplierAdditionalInfoValidationException(List<String> validationErrors) {
        super("Supplier additional info validation failed: " + String.join(", ", validationErrors));
        this.validationErrors = validationErrors;
        this.supplierInfo = null;
    }

    /**
     * Constructor with validation errors and supplier context.
     * 
     * @param validationErrors List of validation error messages
     * @param supplierInfo Supplier context information
     */
    public SupplierAdditionalInfoValidationException(List<String> validationErrors, String supplierInfo) {
        super(String.format("Supplier additional info validation failed for %s: %s", 
                           supplierInfo, String.join(", ", validationErrors)));
        this.validationErrors = validationErrors;
        this.supplierInfo = supplierInfo;
    }

    /**
     * Constructor with single validation error.
     * 
     * @param errorMessage Single validation error message
     */
    public SupplierAdditionalInfoValidationException(String errorMessage) {
        super("Supplier additional info validation failed: " + errorMessage);
        this.validationErrors = List.of(errorMessage);
        this.supplierInfo = null;
    }

    /**
     * Get formatted error message for API responses.
     * 
     * @return Formatted error message
     */
    public String getFormattedMessage() {
        if (supplierInfo != null) {
            return String.format("Validation failed for supplier %s. Errors: %s", 
                               supplierInfo, String.join("; ", validationErrors));
        }
        return String.format("Validation failed. Errors: %s", String.join("; ", validationErrors));
    }

    /**
     * Get validation errors as a single formatted string.
     * 
     * @return Formatted validation errors
     */
    public String getValidationErrorsAsString() {
        return String.join(", ", validationErrors);
    }
}