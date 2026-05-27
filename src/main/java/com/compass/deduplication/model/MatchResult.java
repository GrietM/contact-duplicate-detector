package com.compass.deduplication.model;

public record MatchResult(
        String sourceContactId,
        String matchContactId,
        ConfidenceLevel confidenceLevel,
        int score
) {
}
