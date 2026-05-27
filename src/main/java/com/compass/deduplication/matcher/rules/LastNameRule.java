package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;
import com.compass.deduplication.normalizer.StringNormalizer;

public class LastNameRule implements MatchingRule {

    private static final int SCORE = 25;

    private final StringNormalizer stringNormalizer;

    public LastNameRule(StringNormalizer stringNormalizer) {
        this.stringNormalizer = stringNormalizer;
    }

    @Override
    public int evaluate(Contact source, Contact candidate) {
        String sourceLastName = stringNormalizer.normalize(source.lastName());
        String candidateLastName = stringNormalizer.normalize(candidate.lastName());

        if (sourceLastName.isEmpty() || candidateLastName.isEmpty()) {
            return 0;
        }

        return sourceLastName.equals(candidateLastName) ? SCORE : 0;
    }

    @Override
    public String getRuleName() {
        return "LastNameRule";
    }
}
