package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;
import com.compass.deduplication.normalizer.StringNormalizer;

public class AddressRule implements MatchingRule {

    private static final int SCORE = 45;

    private final StringNormalizer stringNormalizer;

    public AddressRule(StringNormalizer stringNormalizer) {
        this.stringNormalizer = stringNormalizer;
    }

    @Override
    public int evaluate(Contact source, Contact candidate) {
        String sourceAddress = stringNormalizer.normalize(source.address());
        String candidateAddress = stringNormalizer.normalize(candidate.address());

        if (sourceAddress.isEmpty() || candidateAddress.isEmpty()) {
            return 0;
        }

        return sourceAddress.equals(candidateAddress) ? SCORE : 0;
    }

    @Override
    public String getRuleName() {
        return "AddressRule";
    }
}
