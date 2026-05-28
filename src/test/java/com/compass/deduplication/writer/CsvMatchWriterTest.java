package com.compass.deduplication.writer;

import com.compass.deduplication.model.ConfidenceLevel;
import com.compass.deduplication.model.MatchResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvMatchWriterTest {

    private final CsvMatchWriter csvMatchWriter = new CsvMatchWriter();

    @TempDir
    Path tempDir;

    @Test
    void shouldWriteExpectedHeaders() throws IOException {
        Path outputPath = tempDir.resolve("results.csv");

        csvMatchWriter.write(outputPath, List.of());

        List<String> lines = Files.readAllLines(outputPath);
        assertEquals("source_contact_id,match_contact_id,accuracy,score", lines.get(0));
    }

    @Test
    void shouldWriteMatchResultRowsCorrectly() throws IOException {
        Path outputPath = tempDir.resolve("results.csv");

        csvMatchWriter.write(outputPath, List.of(
                new MatchResult("1", "2", ConfidenceLevel.HIGH, 100),
                new MatchResult("3", "4", ConfidenceLevel.MEDIUM, 60)
        ));

        List<String> lines = Files.readAllLines(outputPath);
        assertEquals("1,2,HIGH,100", lines.get(1));
        assertEquals("3,4,MEDIUM,60", lines.get(2));
    }

    @Test
    void shouldSortResultsByScoreDescendingBeforeWriting() throws IOException {
        Path outputPath = tempDir.resolve("results.csv");

        csvMatchWriter.write(outputPath, List.of(
                new MatchResult("3", "4", ConfidenceLevel.MEDIUM, 60),
                new MatchResult("1", "2", ConfidenceLevel.HIGH, 100),
                new MatchResult("5", "6", ConfidenceLevel.LOW, 40)
        ));

        List<String> lines = Files.readAllLines(outputPath);
        assertEquals("1,2,HIGH,100", lines.get(1));
        assertEquals("3,4,MEDIUM,60", lines.get(2));
        assertEquals("5,6,LOW,40", lines.get(3));
    }

    @Test
    void shouldCreateParentDirectoryIfNeeded() throws IOException {
        Path outputPath = tempDir.resolve("nested").resolve("output").resolve("results.csv");

        csvMatchWriter.write(outputPath, List.of(
                new MatchResult("1", "2", ConfidenceLevel.HIGH, 100)
        ));

        assertTrue(Files.exists(outputPath));
    }
}
