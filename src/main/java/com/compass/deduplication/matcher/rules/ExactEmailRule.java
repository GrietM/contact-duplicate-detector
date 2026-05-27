package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;

import java.util.Locale;

public class ExactEmailRule implements MatchingRule {

    private static final int SCORE = 100;

    @Override
    public int evaluate(Contact source, Contact candidate) {
        String sourceEmail = normalizeEmail(source.email());
        String candidateEmail = normalizeEmail(candidate.email());

        if (sourceEmail.isEmpty() || candidateEmail.isEmpty()) {
            return 0;
        }

        return sourceEmail.equals(candidateEmail) ? SCORE : 0;
    }

    @Override
    public String getRuleName() {
        return "ExactEmailRule";
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            return "";
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }
}
