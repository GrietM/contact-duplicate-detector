package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;
import com.compass.deduplication.normalizer.StringNormalizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LastNameRuleTest {

    private final LastNameRule rule = new LastNameRule(new StringNormalizer());

    @Test
    void shouldReturnScoreWhenLastNamesMatchExactly() {
        assertEquals(25, rule.evaluate(contactWithLastName("French"), contactWithLastName("French")));
    }

    @Test
    void shouldIgnoreCaseAndPunctuationWhenComparingLastNames() {
        assertEquals(25, rule.evaluate(contactWithLastName("FRENCH."), contactWithLastName("french")));
    }

    @Test
    void shouldReturnZeroWhenLastNamesDoNotMatch() {
        assertEquals(0, rule.evaluate(contactWithLastName("French"), contactWithLastName("Savage")));
    }

    @Test
    void shouldReturnZeroWhenLastNameIsBlank() {
        assertEquals(0, rule.evaluate(contactWithLastName(" "), contactWithLastName("French")));
    }

    private Contact contactWithLastName(String lastName) {
        return new Contact("1", "Ciara", lastName, "ciara@example.com", "39746", "449 Tellus Rd");
    }
}
