package com.compass.deduplication.classifier;

import com.compass.deduplication.model.ConfidenceLevel;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfidenceClassifierTest {

    private final ConfidenceClassifier classifier = new ConfidenceClassifier();

    @Test
    void shouldReturnHighForExactEmail() {
        assertEquals(Optional.of(ConfidenceLevel.HIGH), classifier.classify(100, Set.of("ExactEmailRule")));
    }

    @Test
    void shouldReturnHighForEmailUsernameAndLastName() {
        assertEquals(Optional.of(ConfidenceLevel.HIGH), classifier.classify(85, Set.of("EmailUsernameRule", "LastNameRule")));
    }

    @Test
    void shouldReturnHighForEmailUsernameAndAddress() {
        assertEquals(Optional.of(ConfidenceLevel.HIGH), classifier.classify(100, Set.of("EmailUsernameRule", "AddressRule")));
    }

    @Test
    void shouldReturnHighForAddressAndLastName() {
        assertEquals(Optional.of(ConfidenceLevel.HIGH), classifier.classify(70, Set.of("AddressRule", "LastNameRule")));
    }

    @Test
    void shouldReturnHighForAddressLastNameAndZipCode() {
        assertEquals(Optional.of(ConfidenceLevel.HIGH), classifier.classify(85, Set.of("AddressRule", "LastNameRule", "ZipCodeRule")));
    }

    @Test
    void shouldReturnHighForFullNameAndAddress() {
        assertEquals(Optional.of(ConfidenceLevel.HIGH), classifier.classify(90, Set.of("FirstNameRule", "LastNameRule", "AddressRule")));
    }

    @Test
    void shouldReturnMediumForEmailUsernameOnly() {
        assertEquals(Optional.of(ConfidenceLevel.MEDIUM), classifier.classify(60, Set.of("EmailUsernameRule")));
    }

    @Test
    void shouldReturnMediumForAddressOnly() {
        assertEquals(Optional.of(ConfidenceLevel.MEDIUM), classifier.classify(45, Set.of("AddressRule")));
    }

    @Test
    void shouldReturnMediumForAddressAndZipCode() {
        assertEquals(Optional.of(ConfidenceLevel.MEDIUM), classifier.classify(60, Set.of("AddressRule", "ZipCodeRule")));
    }

    @Test
    void shouldReturnMediumForFullNameOnly() {
        assertEquals(Optional.of(ConfidenceLevel.MEDIUM), classifier.classify(45, Set.of("FirstNameRule", "LastNameRule")));
    }

    @Test
    void shouldReturnMediumForFullNameAndZipCode() {
        assertEquals(Optional.of(ConfidenceLevel.MEDIUM), classifier.classify(60, Set.of("FirstNameRule", "LastNameRule", "ZipCodeRule")));
    }

    @Test
    void shouldReturnLowForLastNameAndZipCode() {
        assertEquals(Optional.of(ConfidenceLevel.LOW), classifier.classify(40, Set.of("LastNameRule", "ZipCodeRule")));
    }

    @Test
    void shouldReturnEmptyForFirstNameOnly() {
        assertEquals(Optional.empty(), classifier.classify(20, Set.of("FirstNameRule")));
    }

    @Test
    void shouldReturnEmptyForLastNameOnly() {
        assertEquals(Optional.empty(), classifier.classify(25, Set.of("LastNameRule")));
    }

    @Test
    void shouldReturnEmptyForZipCodeOnly() {
        assertEquals(Optional.empty(), classifier.classify(15, Set.of("ZipCodeRule")));
    }

    @Test
    void shouldReturnEmptyForInitialOnly() {
        assertEquals(Optional.empty(), classifier.classify(15, Set.of("InitialNameRule")));
    }
}
