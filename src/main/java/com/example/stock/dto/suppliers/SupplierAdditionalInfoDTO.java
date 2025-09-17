package com.example.stock.dto.suppliers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for Supplier Additional Information containing extended business data.
 * Matches the frontend documentation structure exactly.
 * 
 * @author Development Team
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Extended business information for suppliers")
public class SupplierAdditionalInfoDTO {

    @Valid
    @Schema(description = "Banking and financial details")
    private Finance finance;

    @Valid
    @Schema(description = "Payment preferences and terms")
    private Payment payment;

    @Valid
    @Schema(description = "Multiple contact persons")
    private List<Contact> contacts;

    @Valid
    @Schema(description = "Business operations data")
    private Operations operations;

    @Valid
    @Schema(description = "Business documents")
    private List<DocumentWithUrl> documents;

    @Valid
    @Schema(description = "Tax information")
    private Tax tax;

    /**
     * Finance Information - Banking and financial details
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Schema(description = "Banking and financial details")
    public static class Finance {

        @JsonProperty("account_number")
        @Size(max = 100, message = "Account number cannot exceed 100 characters")
        @Schema(description = "Bank account/IBAN", example = "TN59 1000 0000 0000 0000 0000")
        private String accountNumber;

        @JsonProperty("bank_name")
        @Size(max = 255, message = "Bank name cannot exceed 255 characters")
        @Schema(description = "Bank name", example = "Banque Centrale de Tunisie")
        private String bankName;

        @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter code (e.g., TND, USD, EUR)")
        @Schema(description = "Currency code", example = "TND", allowableValues = { "TND", "USD", "EUR" })
        private String currency;

        /**
         * Validates if finance information is complete for bank transfers
         */
        public boolean isCompleteForBankTransfer() {
            return accountNumber != null && !accountNumber.trim().isEmpty() &&
                    bankName != null && !bankName.trim().isEmpty();
        }
    }

    /**
     * Payment Information - Payment preferences and terms
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Schema(description = "Payment preferences and terms")
    public static class Payment {

        @NotNull(message = "Payment method is required when payment information is provided")
        @Pattern(regexp = "^(Cash|COD|Bank Transfer|Credit)$", message = "Payment method must be one of: Cash, COD, Bank Transfer, Credit")
        @JsonProperty("preferred_method")
        @Schema(description = "Preferred payment method", example = "Bank Transfer", allowableValues = { "Cash", "COD",
                "Bank Transfer", "Credit" })
        private String preferredMethod;

        @Size(max = 500, message = "Payment terms cannot exceed 500 characters")
        @Schema(description = "Payment terms", example = "Net 30 days")
        private String terms;

        /**
         * Checks if this payment method requires banking details
         */
        public boolean requiresBankingDetails() {
            return "Bank Transfer".equals(preferredMethod);
        }

        /**
         * Checks if this payment method should not have banking details
         */
        public boolean shouldNotHaveBankingDetails() {
            return "Cash".equals(preferredMethod) || "COD".equals(preferredMethod);
        }
    }

    /**
     * Contact Information - Contact person details
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Schema(description = "Contact person information")
    public static class Contact {

        @NotBlank(message = "Contact name is required")
        @Size(max = 255, message = "Contact name cannot exceed 255 characters")
        @Schema(description = "Contact person name", example = "Ahmed Ben Ali")
        private String name;

        @Size(max = 100, message = "Role cannot exceed 100 characters")
        @Schema(description = "Job title/role", example = "Sales Manager")
        private String role;

        @Pattern(regexp = "^[+]?[0-9\\s\\-()]{8,20}$", message = "Phone must be 8-20 characters and contain only numbers, spaces, hyphens, parentheses, and optional + prefix")
        @Schema(description = "Contact phone number", example = "+216 71 123 456")
        private String phone;

        @Email(message = "Email must be a valid email address")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        @Schema(description = "Contact email address", example = "ahmed@supplier.com")
        private String email;

        /**
         * Business rule: Contact must have either phone or email (or both)
         */
        public boolean isValid() {
            boolean hasPhone = phone != null && !phone.trim().isEmpty();
            boolean hasEmail = email != null && !email.trim().isEmpty();
            return hasPhone || hasEmail;
        }

        /**
         * Get validation error message if contact is invalid
         */
        public String getValidationError() {
            if (!isValid()) {
                return String.format("Contact '%s' must have either phone or email", name);
            }
            return null;
        }
    }

    /**
     * Operations Information - Business operations data
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Schema(description = "Business operations information")
    public static class Operations {

        @JsonProperty("lead_time_days")
        @Min(value = 0, message = "Lead time cannot be negative")
        @Max(value = 365, message = "Lead time cannot exceed 365 days")
        @Schema(description = "Delivery lead time in days", example = "7")
        private Integer leadTimeDays;

        @JsonProperty("minimum_order_quantity")
        @Min(value = 0, message = "Minimum order quantity cannot be negative")
        @Schema(description = "Minimum order amount", example = "100")
        private Integer minimumOrderQuantity;

        @JsonProperty("delivery_terms")
        @Size(max = 100, message = "Delivery terms cannot exceed 100 characters")
        @Schema(description = "Delivery terms", example = "FOB", allowableValues = { "FOB", "CIF", "EXW", "DDP" })
        private String deliveryTerms;

        @JsonProperty("delivery_address")
        @Size(max = 500, message = "Delivery address cannot exceed 500 characters")
        @Schema(description = "Specific delivery address", example = "Zone Industrielle, Tunis")
        private String deliveryAddress;

        @Schema(description = "Supplier active status", example = "true")
        private Boolean active;

        /**
         * Get default values for operations
         */
        public void setDefaults() {
            if (active == null) {
                active = true;
            }
        }
    }

    /**
     * Document Information - Business documents
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Schema(description = "Business document information")
    public static class DocumentWithUrl {

        @NotBlank(message = "Document name is required")
        @Size(max = 255, message = "Document name cannot exceed 255 characters")
        @Schema(description = "Document name", example = "Contract Agreement 2024")
        private String name;

        @NotBlank(message = "Document URL is required")
        @Size(max = 1000, message = "Document URL cannot exceed 1000 characters")
        @Pattern(regexp = "^https?://.*", message = "Document URL must be a valid HTTP/HTTPS URL")
        @Schema(description = "File URL", example = "https://storage.example.com/contracts/contract_2024.pdf")
        private String url;

        @Size(max = 50, message = "Document type cannot exceed 50 characters")
        @Schema(description = "File type", example = "PDF")
        private String type;

        @Pattern(regexp = "^(contract|certificate|invoice)$", message = "Document category must be one of: contract, certificate, invoice")
        @Schema(description = "Document category", example = "contract", allowableValues = { "contract", "certificate",
                "invoice" })
        private String category;
    }

    /**
     * Tax Information - Tax details
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Schema(description = "Tax information")
    public static class Tax {

        @JsonProperty("tax_id")
        @Size(max = 50, message = "Tax ID cannot exceed 50 characters")
        @Pattern(regexp = "^[A-Z0-9]{1,50}$", message = "Tax ID must contain only uppercase letters and numbers")
        @Schema(description = "Tax identification number", example = "TAX123456789")
        private String taxId;

        @JsonProperty("tax_rate")
        @DecimalMin(value = "0.0", message = "Tax rate cannot be negative")
        @DecimalMax(value = "100.0", message = "Tax rate cannot exceed 100%")
        @Digits(integer = 3, fraction = 2, message = "Tax rate must have at most 3 integer digits and 2 decimal places")
        @Schema(description = "Tax rate percentage (0-100)", example = "19.0")
        private Double taxRate;
    }

    /**
     * Validates the entire additional info structure according to business rules
     */
    public List<String> validateBusinessRules() {
        List<String> errors = new java.util.ArrayList<>();

        try {
            // Validate payment method business rules
            validatePaymentRules(errors);

            // Validate contacts
            validateContacts(errors);

            // Set defaults for operations
            if (operations != null) {
                operations.setDefaults();
            }

        } catch (Exception e) {
            errors.add("Unexpected error during validation: " + e.getMessage());
        }

        return errors;
    }

    /**
     * Validates payment method business rules
     */
    private void validatePaymentRules(List<String> errors) {
        if (payment == null) {
            return; // Payment is optional
        }

        try {
            if (payment.requiresBankingDetails()) {
                if (finance == null || !finance.isCompleteForBankTransfer()) {
                    errors.add(
                            "Bank Transfer payment method requires complete banking information (account number and bank name)");
                }
            } else if (payment.shouldNotHaveBankingDetails()) {
                if (finance != null && finance.isCompleteForBankTransfer()) {
                    errors.add("Cash/COD payment methods should not include banking details");
                }
            }
        } catch (Exception e) {
            errors.add("Error validating payment rules: " + e.getMessage());
        }
    }

    /**
     * Validates contact information
     */
    private void validateContacts(List<String> errors) {
        if (contacts == null || contacts.isEmpty()) {
            return; // Contacts are optional
        }

        try {
            for (int i = 0; i < contacts.size(); i++) {
                Contact contact = contacts.get(i);
                if (contact != null) {
                    String error = contact.getValidationError();
                    if (error != null) {
                        errors.add(String.format("Contact %d: %s", i + 1, error));
                    }
                }
            }
        } catch (Exception e) {
            errors.add("Error validating contacts: " + e.getMessage());
        }
    }
}