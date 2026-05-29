package com.compass.deduplication.classifier;

import com.compass.deduplication.model.ConfidenceLevel;

import java.util.Optional;
import java.util.Set;

public class ConfidenceClassifier {

    private static final String EXACT_EMAIL_RULE = "ExactEmailRule";
    private static final String EMAIL_USERNAME_RULE = "EmailUsernameRule";
    private static final String FIRST_NAME_RULE = "FirstNameRule";
    private static final String LAST_NAME_RULE = "LastNameRule";
    private static final String ADDRESS_RULE = "AddressRule";
    private static final String ZIP_CODE_RULE = "ZipCodeRule";

    public Optional<ConfidenceLevel> classify(int score, Set<String> matchedRuleNames) {
        // Score alone is not enough here; business guardrails decide strong combinations before thresholds apply.
        if (matchedRuleNames.contains(EXACT_EMAIL_RULE)) {
            return Optional.of(ConfidenceLevel.HIGH);
        }

        boolean emailUsername = matchedRuleNames.contains(EMAIL_USERNAME_RULE);
        boolean firstName = matchedRuleNames.contains(FIRST_NAME_RULE);
        boolean lastName = matchedRuleNames.contains(LAST_NAME_RULE);
        boolean address = matchedRuleNames.contains(ADDRESS_RULE);
        boolean zipCode = matchedRuleNames.contains(ZIP_CODE_RULE);
        boolean fullName = firstName && lastName;

        if (emailUsername && address) {
            return Optional.of(ConfidenceLevel.HIGH);
        }

        if (address && lastName) {
            return Optional.of(ConfidenceLevel.HIGH);
        }

        if (address && fullName) {
            return Optional.of(ConfidenceLevel.HIGH);
        }

        if (emailUsername && fullName && zipCode) {
            return Optional.of(ConfidenceLevel.HIGH);
        }

        if (emailUsername || address || fullName) {
            return Optional.of(ConfidenceLevel.MEDIUM);
        }

        if (score >= 30) {
            return Optional.of(ConfidenceLevel.LOW);
        }

        return Optional.empty();
    }
}
