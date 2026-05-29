package com.compass.deduplication.matcher;

import com.compass.deduplication.classifier.ConfidenceClassifier;
import com.compass.deduplication.matcher.rules.MatchingRule;
import com.compass.deduplication.model.ConfidenceLevel;
import com.compass.deduplication.model.Contact;
import com.compass.deduplication.model.MatchResult;
import com.compass.deduplication.scorer.ScoreCalculator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ContactMatcher {

    private final List<MatchingRule> matchingRules;
    private final ScoreCalculator scoreCalculator;
    private final ConfidenceClassifier confidenceClassifier;

    public ContactMatcher(
            List<MatchingRule> matchingRules,
            ScoreCalculator scoreCalculator,
            ConfidenceClassifier confidenceClassifier
    ) {
        this.matchingRules = matchingRules;
        this.scoreCalculator = scoreCalculator;
        this.confidenceClassifier = confidenceClassifier;
    }

    public List<MatchResult> findMatches(List<Contact> contacts) {
        List<MatchResult> matches = new ArrayList<>();

        for (int i = 0; i < contacts.size(); i++) {
            // Start at i + 1 so we never compare a contact with itself and never emit both A-B and B-A.
            for (int j = i + 1; j < contacts.size(); j++) {
                Optional<MatchResult> matchResult = comparePair(contacts.get(i), contacts.get(j));
                matchResult.ifPresent(matches::add);
            }
        }

        return matches;
    }

    private Optional<MatchResult> comparePair(Contact source, Contact candidate) {
        Map<String, Integer> ruleScores = evaluateRules(source, candidate);
        int totalScore = scoreCalculator.calculateTotalScore(ruleScores);
        Set<String> matchedRuleNames = extractMatchedRuleNames(ruleScores);
        Optional<ConfidenceLevel> confidenceLevel = confidenceClassifier.classify(totalScore, matchedRuleNames);

        if (confidenceLevel.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new MatchResult(
                source.contactId(),
                candidate.contactId(),
                confidenceLevel.get(),
                totalScore
        ));
    }

    private Map<String, Integer> evaluateRules(Contact source, Contact candidate) {
        Map<String, Integer> ruleScores = new LinkedHashMap<>();

        for (MatchingRule matchingRule : matchingRules) {
            ruleScores.put(matchingRule.getRuleName(), matchingRule.evaluate(source, candidate));
        }

        return ruleScores;
    }

    private Set<String> extractMatchedRuleNames(Map<String, Integer> ruleScores) {
        Set<String> matchedRuleNames = new LinkedHashSet<>();

        for (Map.Entry<String, Integer> entry : ruleScores.entrySet()) {
            Integer score = entry.getValue();
            if (score != null && score > 0) {
                matchedRuleNames.add(entry.getKey());
            }
        }

        return matchedRuleNames;
    }
}
