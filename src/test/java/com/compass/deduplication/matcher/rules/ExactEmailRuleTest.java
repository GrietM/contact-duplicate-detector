package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExactEmailRuleTest {

    private final ExactEmailRule rule = new ExactEmailRule();

    @Test
    void shouldReturnMaximumScoreWhenEmailsAreExactlyEqual() {
        Contact source = contactWithEmail("john.smith@gmail.com");
        Contact candidate = contactWithEmail("john.smith@gmail.com");

        assertEquals(100, rule.evaluate(source, candidate));
    }

    @Test
    void shouldIgnoreEmailCasing() {
        Contact source = contactWithEmail("john.smith@gmail.com");
        Contact candidate = contactWithEmail("JOHN.SMITH@GMAIL.COM");

        assertEquals(100, rule.evaluate(source, candidate));
    }

    @Test
    void shouldReturnZeroWhenEmailsAreDifferent() {
        Contact source = contactWithEmail("john.smith@gmail.com");
        Contact candidate = contactWithEmail("john.smith@yahoo.com");

        assertEquals(0, rule.evaluate(source, candidate));
    }

    @Test
    void shouldReturnZeroWhenEmailIsBlank() {
        Contact source = contactWithEmail(" ");
        Contact candidate = contactWithEmail("john.smith@gmail.com");

        assertEquals(0, rule.evaluate(source, candidate));
    }

    @Test
    void shouldKeepEmailPunctuationMeaningful() {
        Contact source = contactWithEmail("john.smith@gmail.com");
        Contact candidate = contactWithEmail("johnsmith@gmail.com");

        assertEquals(0, rule.evaluate(source, candidate));
    }

    private Contact contactWithEmail(String email) {
        return new Contact("1", "John", "Smith", email, "12345", "Main St");
    }
}
