package com.compass.deduplication.normalizer;

import java.util.Locale;
import java.util.regex.Pattern;

public class StringNormalizer {

    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("[\\p{Punct}]");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    public String normalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        String normalizedValue = value.trim().toLowerCase(Locale.ROOT);
        normalizedValue = PUNCTUATION_PATTERN.matcher(normalizedValue).replaceAll(" ");
        normalizedValue = WHITESPACE_PATTERN.matcher(normalizedValue).replaceAll(" ");

        return normalizedValue.trim();
    }
}
