package com.compass.deduplication.writer;

import com.compass.deduplication.model.MatchResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class CsvMatchWriter {

    private static final String SOURCE_CONTACT_ID_HEADER = "source_contact_id";
    private static final String MATCH_CONTACT_ID_HEADER = "match_contact_id";
    private static final String ACCURACY_HEADER = "accuracy";
    private static final String SCORE_HEADER = "score";

    public void write(Path outputPath, List<MatchResult> matchResults) throws IOException {
        createParentDirectory(outputPath);

        try (Writer writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat())) {
            for (MatchResult matchResult : sortByScoreDescending(matchResults)) {
                csvPrinter.printRecord(
                        matchResult.sourceContactId(),
                        matchResult.matchContactId(),
                        matchResult.confidenceLevel().name(),
                        matchResult.score()
                );
            }
        }
    }

    private void createParentDirectory(Path outputPath) throws IOException {
        Path parentDirectory = outputPath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }
    }

    private CSVFormat csvFormat() {
        return CSVFormat.DEFAULT.builder()
                .setHeader(
                        SOURCE_CONTACT_ID_HEADER,
                        MATCH_CONTACT_ID_HEADER,
                        ACCURACY_HEADER,
                        SCORE_HEADER
                )
                .build();
    }

    private List<MatchResult> sortByScoreDescending(List<MatchResult> matchResults) {
        // Sorting is an output concern; ContactMatcher preserves discovery order internally.
        return matchResults.stream()
                .sorted(Comparator.comparingInt(MatchResult::score).reversed())
                .toList();
    }
}
