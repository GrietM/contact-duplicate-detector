package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZipCodeRuleTest {

    private final ZipCodeRule rule = new ZipCodeRule();

    @Test
    void shouldReturnScoreWhenZipCodesMatchExactly() {
        assertEquals(15, rule.evaluate(contactWithZipCode("39746"), contactWithZipCode("39746")));
    }

    @Test
    void shouldTrimZipCodesBeforeComparing() {
        assertEquals(15, rule.evaluate(contactWithZipCode("39746 "), contactWithZipCode("39746")));
    }

    @Test
    void shouldReturnZeroWhenZipCodesDoNotMatch() {
        assertEquals(0, rule.evaluate(contactWithZipCode("39746"), contactWithZipCode("82025")));
    }

    @Test
    void shouldReturnZeroWhenZipCodeIsBlank() {
        assertEquals(0, rule.evaluate(contactWithZipCode(" "), contactWithZipCode("39746")));
    }

    private Contact contactWithZipCode(String zipCode) {
        return new Contact("1", "Ciara", "French", "ciara@example.com", zipCode, "449 Tellus Rd");
    }
}
