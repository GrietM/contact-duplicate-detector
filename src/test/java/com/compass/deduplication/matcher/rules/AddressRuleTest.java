package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;
import com.compass.deduplication.normalizer.StringNormalizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressRuleTest {

    private final AddressRule rule = new AddressRule(new StringNormalizer());

    @Test
    void shouldReturnScoreWhenAddressesMatchExactly() {
        assertEquals(45, rule.evaluate(contactWithAddress("449-6990 Tellus. Rd."), contactWithAddress("449-6990 Tellus. Rd.")));
    }

    @Test
    void shouldIgnorePunctuationCaseAndSpacingWhenComparingAddresses() {
        assertEquals(45, rule.evaluate(contactWithAddress("449-6990 Tellus. Rd."), contactWithAddress("449 6990 tellus rd")));
    }

    @Test
    void shouldReturnZeroWhenAddressesDoNotMatch() {
        assertEquals(0, rule.evaluate(contactWithAddress("449-6990 Tellus. Rd."), contactWithAddress("735-3498 Magna. Street")));
    }

    @Test
    void shouldReturnZeroWhenAddressIsBlank() {
        assertEquals(0, rule.evaluate(contactWithAddress(" "), contactWithAddress("449-6990 Tellus. Rd.")));
    }

    private Contact contactWithAddress(String address) {
        return new Contact("1", "Ciara", "French", "ciara@example.com", "39746", address);
    }
}
