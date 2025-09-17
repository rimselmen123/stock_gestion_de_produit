package com.example.stock.validation;

import com.example.stock.dto.suppliers.SupplierAdditionalInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Professional validator for Supplier Additional Info with comprehensive business rules.
 * Provides detailed validation messages and robust error handling.
 * 
 * @author Development Team
 * @since 1.0
 */
@Component
@Slf4j
public class SupplierAdditionalInfoValidator {

    /**
     * Validate additional info according to business rules with comprehensive error handling.
     * 
     * @param additionalInfo Additional info to validate
     * @return List of validation error messages, empty if valid
     */
    public List<String> validate(SupplierAdditionalInfoDTO additionalInfo) {
        List<String> errors = new ArrayList<>();

        if (additionalInfo == null) {
            log.debug("Additional info is null - skipping validation (optional field)");
            return errors; // Additional info is optional
        }

        try {
            log.debug("Starting validation of supplier additional info");

            // Validate payment method business rules
            validatePaymentRules(additionalInfo, errors);

            // Validate contacts
            validateContacts(additionalInfo, errors);

            // Validate operations
            validateOperations(additionalInfo, errors);

            // Validate documents
            validateDocuments(additionalInfo, errors);

            // Validate tax information
            validateTax(additionalInfo, errors);

            // Validate finance information
            validateFinance(additionalInfo, errors);

            log.debug("Validation completed. Found {} errors", errors.size());
            
            if (!errors.isEmpty()) {
                log.warn("Validation failed with {} errors: {}", errors.size(), String.join(", ", errors));
            }

        } catch (Exception e) {
            String errorMsg = "Unexpected error during additional info validation: " + e.getMessage();
            log.error(errorMsg, e);
            errors.add(errorMsg);
        }

        return errors;
    }

    /**
     * Validate payment method business rules with detailed error messages.
     */
    private void validatePaymentRules(SupplierAdditionalInfoDTO additionalInfo, List<String> errors) {
        try {
            SupplierAdditionalInfoDTO.Payment payment = additionalInfo.getPayment();
            if (payment == null) {
                log.debug("Payment info is null - skipping payment validation");
                return;
            }

            log.debug("Validating payment rules for method: {}", payment.getPreferredMethod());

            SupplierAdditionalInfoDTO.Finance finance = additionalInfo.getFinance();

            // Bank Transfer requires complete banking information
            if (payment.requiresBankingDetails()) {
                if (finance == null) {
                    errors.add("Bank Transfer payment method requires finance information to be provided");
                } else if (!finance.isCompleteForBankTransfer()) {
                    List<String> missingFields = new ArrayList<>();
                    if (finance.getAccountNumber() == null || finance.getAccountNumber().trim().isEmpty()) {
                        missingFields.add("account number");
                    }
                    if (finance.getBankName() == null || finance.getBankName().trim().isEmpty()) {
                        missingFields.add("bank name");
                    }
                    errors.add("Bank Transfer payment method requires the following finance fields: " + 
                              String.join(", ", missingFields));
                }
            }

            // Cash/COD should not have banking details
            if (payment.shouldNotHaveBankingDetails()) {
                if (finance != null && finance.isCompleteForBankTransfer()) {
                    errors.add(String.format("%s payment method should not include banking details (account number and bank name)", 
                                           payment.getPreferredMethod()));
                }
            }

            // Validate payment terms if provided
            if (payment.getTerms() != null && payment.getTerms().trim().length() > 500) {
                errors.add("Payment terms cannot exceed 500 characters");
            }

        } catch (Exception e) {
            String errorMsg = "Error validating payment rules: " + e.getMessage();
            log.error(errorMsg, e);
            errors.add(errorMsg);
        }
    }

    /**
     * Validate contact information with detailed error messages.
     */
    private void validateContacts(SupplierAdditionalInfoDTO additionalInfo, List<String> errors) {
        try {
            List<SupplierAdditionalInfoDTO.Contact> contacts = additionalInfo.getContacts();
            if (contacts == null || contacts.isEmpty()) {
                log.debug("No contacts provided - skipping contact validation");
                return;
            }

            log.debug("Validating {} contacts", contacts.size());

            for (int i = 0; i < contacts.size(); i++) {
                SupplierAdditionalInfoDTO.Contact contact = contacts.get(i);
                if (contact == null) {
                    errors.add(String.format("Contact %d is null", i + 1));
                    continue;
                }

                // Validate contact completeness
                if (!contact.isValid()) {
                    errors.add(String.format("Contact %d ('%s') must have either phone or email", 
                                           i + 1, contact.getName()));
                }

                // Validate contact name
                if (contact.getName() == null || contact.getName().trim().isEmpty()) {
                    errors.add(String.format("Contact %d must have a name", i + 1));
                }

                // Validate phone format if provided
                if (contact.getPhone() != null && !contact.getPhone().trim().isEmpty()) {
                    if (!contact.getPhone().matches("^[+]?[0-9\\s\\-()]{8,20}$")) {
                        errors.add(String.format("Contact %d ('%s') has invalid phone format", 
                                                i + 1, contact.getName()));
                    }
                }

                // Validate email format if provided
                if (contact.getEmail() != null && !contact.getEmail().trim().isEmpty()) {
                    if (!contact.getEmail().matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")) {
                        errors.add(String.format("Contact %d ('%s') has invalid email format", 
                                                i + 1, contact.getName()));
                    }
                }
            }

        } catch (Exception e) {
            String errorMsg = "Error validating contacts: " + e.getMessage();
            log.error(errorMsg, e);
            errors.add(errorMsg);
        }
    }

    /**
     * Validate operations information.
     */
    private void validateOperations(SupplierAdditionalInfoDTO additionalInfo, List<String> errors) {
        try {
            SupplierAdditionalInfoDTO.Operations operations = additionalInfo.getOperations();
            if (operations == null) {
                log.debug("Operations info is null - skipping operations validation");
                return;
            }

            log.debug("Validating operations information");

            // Validate lead time
            if (operations.getLeadTimeDays() != null && operations.getLeadTimeDays() < 0) {
                errors.add("Lead time days cannot be negative");
            }
            if (operations.getLeadTimeDays() != null && operations.getLeadTimeDays() > 365) {
                errors.add("Lead time days cannot exceed 365 days");
            }

            // Validate minimum order quantity
            if (operations.getMinimumOrderQuantity() != null && operations.getMinimumOrderQuantity() < 0) {
                errors.add("Minimum order quantity cannot be negative");
            }

            // Set default for active if not provided
            if (operations.getActive() == null) {
                operations.setActive(true);
                log.debug("Set default active status to true");
            }

        } catch (Exception e) {
            String errorMsg = "Error validating operations: " + e.getMessage();
            log.error(errorMsg, e);
            errors.add(errorMsg);
        }
    }

    /**
     * Validate documents information.
     */
    private void validateDocuments(SupplierAdditionalInfoDTO additionalInfo, List<String> errors) {
        try {
            List<SupplierAdditionalInfoDTO.DocumentWithUrl> documents = additionalInfo.getDocuments();
            if (documents == null || documents.isEmpty()) {
                log.debug("No documents provided - skipping document validation");
                return;
            }

            log.debug("Validating {} documents", documents.size());

            for (int i = 0; i < documents.size(); i++) {
                SupplierAdditionalInfoDTO.DocumentWithUrl document = documents.get(i);
                if (document == null) {
                    errors.add(String.format("Document %d is null", i + 1));
                    continue;
                }

                // Validate document name
                if (document.getName() == null || document.getName().trim().isEmpty()) {
                    errors.add(String.format("Document %d must have a name", i + 1));
                }

                // Validate document URL
                if (document.getUrl() == null || document.getUrl().trim().isEmpty()) {
                    errors.add(String.format("Document %d ('%s') must have a URL", 
                                           i + 1, document.getName()));
                } else if (!document.getUrl().matches("^https?://.*")) {
                    errors.add(String.format("Document %d ('%s') must have a valid HTTP/HTTPS URL", 
                                           i + 1, document.getName()));
                }

                // Validate document category if provided
                if (document.getCategory() != null && !document.getCategory().trim().isEmpty()) {
                    if (!document.getCategory().matches("^(contract|certificate|invoice)$")) {
                        errors.add(String.format("Document %d ('%s') has invalid category. Must be: contract, certificate, or invoice", 
                                                i + 1, document.getName()));
                    }
                }
            }

        } catch (Exception e) {
            String errorMsg = "Error validating documents: " + e.getMessage();
            log.error(errorMsg, e);
            errors.add(errorMsg);
        }
    }

    /**
     * Validate tax information.
     */
    private void validateTax(SupplierAdditionalInfoDTO additionalInfo, List<String> errors) {
        try {
            SupplierAdditionalInfoDTO.Tax tax = additionalInfo.getTax();
            if (tax == null) {
                log.debug("Tax info is null - skipping tax validation");
                return;
            }

            log.debug("Validating tax information");

            // Validate tax ID format if provided
            if (tax.getTaxId() != null && !tax.getTaxId().trim().isEmpty()) {
                if (!tax.getTaxId().matches("^[A-Z0-9]{1,50}$")) {
                    errors.add("Tax ID must contain only uppercase letters and numbers (max 50 characters)");
                }
            }

            // Validate tax rate if provided
            if (tax.getTaxRate() != null) {
                if (tax.getTaxRate() < 0) {
                    errors.add("Tax rate cannot be negative");
                }
                if (tax.getTaxRate() > 100) {
                    errors.add("Tax rate cannot exceed 100%");
                }
            }

        } catch (Exception e) {
            String errorMsg = "Error validating tax information: " + e.getMessage();
            log.error(errorMsg, e);
            errors.add(errorMsg);
        }
    }

    /**
     * Validate finance information.
     */
    private void validateFinance(SupplierAdditionalInfoDTO additionalInfo, List<String> errors) {
        try {
            SupplierAdditionalInfoDTO.Finance finance = additionalInfo.getFinance();
            if (finance == null) {
                log.debug("Finance info is null - skipping finance validation");
                return;
            }

            log.debug("Validating finance information");

            // Validate currency format if provided
            if (finance.getCurrency() != null && !finance.getCurrency().trim().isEmpty()) {
                if (!finance.getCurrency().matches("^[A-Z]{3}$")) {
                    errors.add("Currency must be a valid 3-letter code (e.g., TND, USD, EUR)");
                }
            }

            // Validate account number length if provided
            if (finance.getAccountNumber() != null && finance.getAccountNumber().length() > 100) {
                errors.add("Account number cannot exceed 100 characters");
            }

            // Validate bank name length if provided
            if (finance.getBankName() != null && finance.getBankName().length() > 255) {
                errors.add("Bank name cannot exceed 255 characters");
            }

        } catch (Exception e) {
            String errorMsg = "Error validating finance information: " + e.getMessage();
            log.error(errorMsg, e);
            errors.add(errorMsg);
        }
    }
}