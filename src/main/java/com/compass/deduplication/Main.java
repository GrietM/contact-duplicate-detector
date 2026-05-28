package com.compass.deduplication;

import com.compass.deduplication.classifier.ConfidenceClassifier;
import com.compass.deduplication.matcher.ContactMatcher;
import com.compass.deduplication.matcher.rules.AddressRule;
import com.compass.deduplication.matcher.rules.EmailUsernameRule;
import com.compass.deduplication.matcher.rules.ExactEmailRule;
import com.compass.deduplication.matcher.rules.FirstNameRule;
import com.compass.deduplication.matcher.rules.InitialNameRule;
import com.compass.deduplication.matcher.rules.LastNameRule;
import com.compass.deduplication.matcher.rules.MatchingRule;
import com.compass.deduplication.matcher.rules.ZipCodeRule;
import com.compass.deduplication.model.Contact;
import com.compass.deduplication.model.MatchResult;
import com.compass.deduplication.normalizer.StringNormalizer;
import com.compass.deduplication.parser.CsvContactParser;
import com.compass.deduplication.scorer.ScoreCalculator;
import com.compass.deduplication.writer.CsvMatchWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {

    private static final Path DEFAULT_OUTPUT_PATH = Path.of("output", "results.csv");

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar contact-duplicate-detector.jar <input-csv-path> [output-csv-path]");
            return;
        }

        Path inputPath = Path.of(args[0]);
        Path outputPath = args.length > 1 ? Path.of(args[1]) : DEFAULT_OUTPUT_PATH;

        CsvContactParser csvContactParser = new CsvContactParser();
        CsvMatchWriter csvMatchWriter = new CsvMatchWriter();
        ContactMatcher contactMatcher = buildContactMatcher();

        try {
            List<Contact> contacts = csvContactParser.parse(inputPath);
            List<MatchResult> matchResults = contactMatcher.findMatches(contacts);
            csvMatchWriter.write(outputPath, matchResults);

            System.out.println("Loaded contacts: " + contacts.size());
            System.out.println("Matches written: " + matchResults.size());
            System.out.println("Output file: " + outputPath);
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println("Error: " + exception.getMessage());
        }
    }

    private static ContactMatcher buildContactMatcher() {
        StringNormalizer stringNormalizer = new StringNormalizer();
        List<MatchingRule> matchingRules = List.of(
                new ExactEmailRule(),
                new EmailUsernameRule(),
                new AddressRule(stringNormalizer),
                new LastNameRule(stringNormalizer),
                new FirstNameRule(stringNormalizer),
                new InitialNameRule(stringNormalizer),
                new ZipCodeRule()
        );

        return new ContactMatcher(
                matchingRules,
                new ScoreCalculator(),
                new ConfidenceClassifier()
        );
    }
}
