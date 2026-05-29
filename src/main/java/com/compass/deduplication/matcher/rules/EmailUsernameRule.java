package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;

import java.util.Locale;

public class EmailUsernameRule implements MatchingRule {

    private static final int SCORE = 60;

    @Override
    public int evaluate(Contact source, Contact candidate) {
        String sourceEmail = normalizeEmail(source.email());
        String candidateEmail = normalizeEmail(candidate.email());

        if (sourceEmail.isEmpty() || candidateEmail.isEmpty()) {
            return 0;
        }

        // Exact full-email matches belong to ExactEmailRule so the same evidence is not counted twice.
        if (sourceEmail.equals(candidateEmail)) {
            return 0;
        }

        String sourceUsername = extractUsername(sourceEmail);
        String candidateUsername = extractUsername(candidateEmail);

        if (sourceUsername.isEmpty() || candidateUsername.isEmpty()) {
            return 0;
        }

        return sourceUsername.equals(candidateUsername) ? SCORE : 0;
    }

    @Override
    public String getRuleName() {
        return "EmailUsernameRule";
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            return "";
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String extractUsername(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex == email.length() - 1) {
            return "";
        }

        return email.substring(0, atIndex);
    }
}
