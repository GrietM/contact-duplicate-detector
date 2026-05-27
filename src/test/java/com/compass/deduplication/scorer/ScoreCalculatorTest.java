package com.compass.deduplication.scorer;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreCalculatorTest {

    private final ScoreCalculator scoreCalculator = new ScoreCalculator();

    @Test
    void shouldReturnHundredForExactEmail() {
        assertEquals(100, scoreCalculator.calculateTotalScore(Map.of(
                "ExactEmailRule", 100
        )));
    }

    @Test
    void shouldReturnSixtyForEmailUsernameOnly() {
        assertEquals(60, scoreCalculator.calculateTotalScore(Map.of(
                "EmailUsernameRule", 60
        )));
    }

    @Test
    void shouldReturnFortyFiveForAddressOnly() {
        assertEquals(45, scoreCalculator.calculateTotalScore(Map.of(
                "AddressRule", 45
        )));
    }

    @Test
    void shouldReturnFortyForLastNameAndZipCode() {
        assertEquals(40, scoreCalculator.calculateTotalScore(Map.of(
                "LastNameRule", 25,
                "ZipCodeRule", 15
        )));
    }

    @Test
    void shouldReturnSixtyForFirstNameLastNameAndZipCode() {
        assertEquals(60, scoreCalculator.calculateTotalScore(Map.of(
                "FirstNameRule", 20,
                "LastNameRule", 25,
                "ZipCodeRule", 15
        )));
    }

    @Test
    void shouldReturnSeventyForAddressAndLastName() {
        assertEquals(70, scoreCalculator.calculateTotalScore(Map.of(
                "AddressRule", 45,
                "LastNameRule", 25
        )));
    }

    @Test
    void shouldReturnEightyFiveForEmailUsernameAndLastName() {
        assertEquals(85, scoreCalculator.calculateTotalScore(Map.of(
                "EmailUsernameRule", 60,
                "LastNameRule", 25
        )));
    }

    @Test
    void shouldCapScoreAtOneHundred() {
        assertEquals(100, scoreCalculator.calculateTotalScore(Map.of(
                "EmailUsernameRule", 60,
                "AddressRule", 45,
                "LastNameRule", 25
        )));
    }
}
