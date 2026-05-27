package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailUsernameRuleTest {

    private final EmailUsernameRule rule = new EmailUsernameRule();

    @Test
    void shouldReturnScoreWhenUsernamesMatchAndDomainsDiffer() {
        Contact source = contactWithEmail("john.smith@gmail.com");
        Contact candidate = contactWithEmail("john.smith@yahoo.com");

        assertEquals(60, rule.evaluate(source, candidate));
    }

    @Test
    void shouldIgnoreCasingWhenComparingUsernames() {
        Contact source = contactWithEmail("john.smith@gmail.com");
        Contact candidate = contactWithEmail("JOHN.SMITH@YAHOO.COM");

        assertEquals(60, rule.evaluate(source, candidate));
    }

    @Test
    void shouldReturnZeroWhenFullNormalizedEmailsAreExactlyEqual() {
        Contact source = contactWithEmail("john.smith@gmail.com");
        Contact candidate = contactWithEmail("JOHN.SMITH@GMAIL.COM");

        assertEquals(0, rule.evaluate(source, candidate));
    }

    @Test
    void shouldReturnZeroWhenUsernamesAreDifferent() {
        Contact source = contactWithEmail("john.smith@gmail.com");
        Contact candidate = contactWithEmail("johnsmith@yahoo.com");

        assertEquals(0, rule.evaluate(source, candidate));
    }

    @Test
    void shouldReturnZeroWhenEmailIsInvalid() {
        Contact source = contactWithEmail("john.smith");
        Contact candidate = contactWithEmail("john.smith@yahoo.com");

        assertEquals(0, rule.evaluate(source, candidate));
    }

    @Test
    void shouldReturnZeroWhenEmailIsBlank() {
        Contact source = contactWithEmail(" ");
        Contact candidate = contactWithEmail("john.smith@yahoo.com");

        assertEquals(0, rule.evaluate(source, candidate));
    }

    @Test
    void shouldKeepUsernamePunctuationMeaningful() {
        Contact source = contactWithEmail("john.smith@gmail.com");
        Contact candidate = contactWithEmail("johnsmith@yahoo.com");

        assertEquals(0, rule.evaluate(source, candidate));
    }

    private Contact contactWithEmail(String email) {
        return new Contact("1", "John", "Smith", email, "12345", "Main St");
    }
}
