package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;
import com.compass.deduplication.normalizer.StringNormalizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FirstNameRuleTest {

    private final FirstNameRule rule = new FirstNameRule(new StringNormalizer());

    @Test
    void shouldReturnScoreWhenFirstNamesMatchExactly() {
        assertEquals(20, rule.evaluate(contactWithFirstName("Ciara"), contactWithFirstName("Ciara")));
    }

    @Test
    void shouldIgnoreCaseAndPunctuationWhenComparingFirstNames() {
        assertEquals(20, rule.evaluate(contactWithFirstName("CIARA."), contactWithFirstName("ciara")));
    }

    @Test
    void shouldReturnZeroWhenFirstNamesDoNotMatch() {
        assertEquals(0, rule.evaluate(contactWithFirstName("Ciara"), contactWithFirstName("Victor")));
    }

    @Test
    void shouldReturnZeroWhenFirstNameIsBlank() {
        assertEquals(0, rule.evaluate(contactWithFirstName(" "), contactWithFirstName("Ciara")));
    }

    private Contact contactWithFirstName(String firstName) {
        return new Contact("1", firstName, "French", "ciara@example.com", "39746", "449 Tellus Rd");
    }
}
