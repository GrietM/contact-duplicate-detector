package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;

public class ZipCodeRule implements MatchingRule {

    private static final int SCORE = 15;

    @Override
    public int evaluate(Contact source, Contact candidate) {
        String sourceZipCode = normalizeZipCode(source.zipCode());
        String candidateZipCode = normalizeZipCode(candidate.zipCode());

        if (sourceZipCode.isEmpty() || candidateZipCode.isEmpty()) {
            return 0;
        }

        return sourceZipCode.equals(candidateZipCode) ? SCORE : 0;
    }

    @Override
    public String getRuleName() {
        return "ZipCodeRule";
    }

    private String normalizeZipCode(String zipCode) {
        if (zipCode == null || zipCode.isBlank()) {
            return "";
        }

        return zipCode.trim();
    }
}
