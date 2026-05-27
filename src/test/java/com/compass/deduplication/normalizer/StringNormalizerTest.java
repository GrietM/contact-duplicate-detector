package com.compass.deduplication.normalizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringNormalizerTest {

    private final StringNormalizer normalizer = new StringNormalizer();

    @Test
    void shouldTrimConvertToLowercaseAndRemoveTrailingPunctuation() {
        assertEquals("tellus rd", normalizer.normalize(" Tellus Rd. "));
    }

    @Test
    void shouldCollapseMultipleSpaces() {
        assertEquals("john smith", normalizer.normalize("JOHN   SMITH"));
    }

    @Test
    void shouldReplacePunctuationWithSpacesBeforeCollapsingWhitespace() {
        assertEquals("449 6990 tellus rd", normalizer.normalize("449-6990 Tellus. Rd."));
    }

    @Test
    void shouldReturnEmptyStringForNullValue() {
        assertEquals("", normalizer.normalize(null));
    }

    @Test
    void shouldReturnEmptyStringForEmptyValue() {
        assertEquals("", normalizer.normalize(""));
    }

    @Test
    void shouldReturnEmptyStringForPunctuationOnlyValue() {
        assertEquals("", normalizer.normalize(" , . - "));
    }
}
