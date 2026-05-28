package com.compass.deduplication.matcher;

import com.compass.deduplication.classifier.ConfidenceClassifier;
import com.compass.deduplication.matcher.rules.AddressRule;
import com.compass.deduplication.matcher.rules.EmailUsernameRule;
import com.compass.deduplication.matcher.rules.ExactEmailRule;
import com.compass.deduplication.matcher.rules.FirstNameRule;
import com.compass.deduplication.matcher.rules.InitialNameRule;
import com.compass.deduplication.matcher.rules.LastNameRule;
import com.compass.deduplication.matcher.rules.MatchingRule;
import com.compass.deduplication.matcher.rules.ZipCodeRule;
import com.compass.deduplication.model.ConfidenceLevel;
import com.compass.deduplication.model.Contact;
import com.compass.deduplication.model.MatchResult;
import com.compass.deduplication.normalizer.StringNormalizer;
import com.compass.deduplication.scorer.ScoreCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactMatcherTest {

    private final ContactMatcher contactMatcher = new ContactMatcher(
            matchingRules(),
            new ScoreCalculator(),
            new ConfidenceClassifier()
    );

    @Test
    void shouldReturnNoSelfMatchesWhenSingleContactIsProvided() {
        List<MatchResult> matches = contactMatcher.findMatches(List.of(
                contact("1", "Ciara", "French", "ciara@example.com", "39746", "449 Tellus Rd")
        ));

        assertTrue(matches.isEmpty());
    }

    @Test
    void shouldAvoidDuplicatedInvertedPairs() {
        List<MatchResult> matches = contactMatcher.findMatches(List.of(
                contact("1", "Ciara", "French", "ciara@example.com", "39746", "449 Tellus Rd"),
                contact("2", "Ciara", "French", "ciara@example.com", "39746", "449 Tellus Rd")
        ));

        assertEquals(1, matches.size());
        assertEquals("1", matches.get(0).sourceContactId());
        assertEquals("2", matches.get(0).matchContactId());
    }

    @Test
    void shouldCreateHighMatchResultForExactEmail() {
        List<MatchResult> matches = contactMatcher.findMatches(List.of(
                contact("1", "Ciara", "French", "ciara@example.com", "39746", "449 Tellus Rd"),
                contact("2", "Victor", "Savage", "CIARA@example.com", "82025", "735 Magna Street")
        ));

        assertEquals(List.of(new MatchResult("1", "2", ConfidenceLevel.HIGH, 100)), matches);
    }

    @Test
    void shouldCreateHighMatchResultForUsernameAddressAndLastName() {
        List<MatchResult> matches = contactMatcher.findMatches(List.of(
                contact("1", "Ciara", "French", "mollis.lectus.pede@outlook.net", "39746", "449-6990 Tellus. Rd."),
                contact("2", "C", "French", "mollis.lectus.pede@yahoo.net", "10214", "449 6990 tellus rd")
        ));

        assertEquals(List.of(new MatchResult("1", "2", ConfidenceLevel.HIGH, 100)), matches);
    }

    @Test
    void shouldCreateMediumMatchResultForAddressOnly() {
        List<MatchResult> matches = contactMatcher.findMatches(List.of(
                contact("1", "Ciara", "French", "ciara@example.com", "39746", "449-6990 Tellus. Rd."),
                contact("2", "Victor", "Savage", "victor@example.com", "82025", "449 6990 tellus rd")
        ));

        assertEquals(List.of(new MatchResult("1", "2", ConfidenceLevel.MEDIUM, 45)), matches);
    }

    @Test
    void shouldCreateLowMatchResultForLastNameAndZipCode() {
        List<MatchResult> matches = contactMatcher.findMatches(List.of(
                contact("1", "Ciara", "French", "ciara@example.com", "39746", "449 Tellus Rd"),
                contact("2", "Victor", "French", "victor@example.com", "39746", "735 Magna Street")
        ));

        assertEquals(List.of(new MatchResult("1", "2", ConfidenceLevel.LOW, 40)), matches);
    }

    @Test
    void shouldSkipPairsClassifiedAsEmpty() {
        List<MatchResult> matches = contactMatcher.findMatches(List.of(
                contact("1", "Ciara", "French", "ciara@example.com", "39746", "449 Tellus Rd"),
                contact("2", "Ciara", "Savage", "victor@example.com", "82025", "735 Magna Street")
        ));

        assertTrue(matches.isEmpty());
    }

    @Test
    void shouldAllowOneContactToHaveMultipleMatchesInDiscoveryOrder() {
        List<MatchResult> matches = contactMatcher.findMatches(List.of(
                contact("1", "Ciara", "French", "ciara@example.com", "39746", "449 Tellus Rd"),
                contact("2", "Victor", "Savage", "CIARA@example.com", "82025", "735 Magna Street"),
                contact("3", "Someone", "Else", "someone@example.com", "99999", "449 Tellus Rd")
        ));

        assertEquals(2, matches.size());
        assertEquals(new MatchResult("1", "2", ConfidenceLevel.HIGH, 100), matches.get(0));
        assertEquals(new MatchResult("1", "3", ConfidenceLevel.MEDIUM, 45), matches.get(1));
    }

    private List<MatchingRule> matchingRules() {
        StringNormalizer stringNormalizer = new StringNormalizer();

        return List.of(
                new ExactEmailRule(),
                new EmailUsernameRule(),
                new AddressRule(stringNormalizer),
                new LastNameRule(stringNormalizer),
                new FirstNameRule(stringNormalizer),
                new InitialNameRule(stringNormalizer),
                new ZipCodeRule()
        );
    }

    private Contact contact(
            String contactId,
            String firstName,
            String lastName,
            String email,
            String zipCode,
            String address
    ) {
        return new Contact(contactId, firstName, lastName, email, zipCode, address);
    }
}
