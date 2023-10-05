package com.andy.customer.exception;

public enum ErrorMessages {
    EMAIL_ALREADY_TAKEN("Email already taken."),
    CUSTOMER_NOT_FOUND("Customer with [%s] not found.");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
