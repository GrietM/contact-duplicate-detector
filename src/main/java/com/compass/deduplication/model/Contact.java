package com.compass.deduplication.model;

public record Contact(
        String contactId,
        String firstName,
        String lastName,
        String email,
        String zipCode,
        String address
) {
    public Contact {
        contactId = defaultValue(contactId);
        firstName = defaultValue(firstName);
        lastName = defaultValue(lastName);
        email = defaultValue(email);
        zipCode = defaultValue(zipCode);
        address = defaultValue(address);
    }

    private static String defaultValue(String value) {
        return value == null ? "" : value;
    }
}
