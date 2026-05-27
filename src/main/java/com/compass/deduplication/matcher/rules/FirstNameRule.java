package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;
import com.compass.deduplication.normalizer.StringNormalizer;

public class FirstNameRule implements MatchingRule {

    private static final int SCORE = 20;

    private final StringNormalizer stringNormalizer;

    public FirstNameRule(StringNormalizer stringNormalizer) {
        this.stringNormalizer = stringNormalizer;
    }

    @Override
    public int evaluate(Contact source, Contact candidate) {
        String sourceFirstName = stringNormalizer.normalize(source.firstName());
        String candidateFirstName = stringNormalizer.normalize(candidate.firstName());

        if (sourceFirstName.isEmpty() || candidateFirstName.isEmpty()) {
            return 0;
        }

        return sourceFirstName.equals(candidateFirstName) ? SCORE : 0;
    }

    @Override
    public String getRuleName() {
        return "FirstNameRule";
    }
}
