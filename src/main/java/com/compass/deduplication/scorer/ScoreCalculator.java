package com.compass.deduplication.scorer;

import java.util.Map;

public class ScoreCalculator {

    private static final int MAX_SCORE = 100;

    public int calculateTotalScore(Map<String, Integer> ruleScores) {
        int totalScore = ruleScores.values().stream()
                .filter(score -> score != null && score > 0)
                .mapToInt(Integer::intValue)
                .sum();

        // Scores rank relative strength, so anything above 100 stops adding useful signal.
        return Math.min(totalScore, MAX_SCORE);
    }
}
