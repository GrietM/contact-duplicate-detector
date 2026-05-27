package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;
import com.compass.deduplication.normalizer.StringNormalizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InitialNameRuleTest {

    private final InitialNameRule rule = new InitialNameRule(new StringNormalizer());

    @Test
    void shouldReturnScoreWhenSourceIsInitialAndCandidateIsFullName() {
        assertEquals(15, rule.evaluate(contactWithFirstName("C"), contactWithFirstName("Ciara")));
    }

    @Test
    void shouldReturnScoreWhenCandidateIsInitialAndSourceIsFullName() {
        assertEquals(15, rule.evaluate(contactWithFirstName("Ciara"), contactWithFirstName("C")));
    }

    @Test
    void shouldReturnZeroWhenInitialDoesNotMatchFullName() {
        assertEquals(0, rule.evaluate(contactWithFirstName("Ciara"), contactWithFirstName("D")));
    }

    @Test
    void shouldReturnZeroForTwoInitials() {
        assertEquals(0, rule.evaluate(contactWithFirstName("C"), contactWithFirstName("C")));
    }

    @Test
    void shouldReturnZeroForTwoEqualFullNames() {
        assertEquals(0, rule.evaluate(contactWithFirstName("Ciara"), contactWithFirstName("Ciara")));
    }

    @Test
    void shouldReturnZeroWhenFirstNameIsBlank() {
        assertEquals(0, rule.evaluate(contactWithFirstName(" "), contactWithFirstName("Ciara")));
    }

    private Contact contactWithFirstName(String firstName) {
        return new Contact("1", firstName, "French", "ciara@example.com", "39746", "449 Tellus Rd");
    }
}
