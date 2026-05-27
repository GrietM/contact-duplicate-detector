package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;
import com.compass.deduplication.normalizer.StringNormalizer;

public class InitialNameRule implements MatchingRule {

    private static final int SCORE = 15;

    private final StringNormalizer stringNormalizer;

    public InitialNameRule(StringNormalizer stringNormalizer) {
        this.stringNormalizer = stringNormalizer;
    }

    @Override
    public int evaluate(Contact source, Contact candidate) {
        String sourceFirstName = stringNormalizer.normalize(source.firstName());
        String candidateFirstName = stringNormalizer.normalize(candidate.firstName());

        if (sourceFirstName.isEmpty() || candidateFirstName.isEmpty()) {
            return 0;
        }

        if (sourceFirstName.length() == 1 && candidateFirstName.length() > 1) {
            return sourceFirstName.charAt(0) == candidateFirstName.charAt(0) ? SCORE : 0;
        }

        if (candidateFirstName.length() == 1 && sourceFirstName.length() > 1) {
            return candidateFirstName.charAt(0) == sourceFirstName.charAt(0) ? SCORE : 0;
        }

        return 0;
    }

    @Override
    public String getRuleName() {
        return "InitialNameRule";
    }
}
