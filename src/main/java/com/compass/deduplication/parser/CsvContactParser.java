package com.compass.deduplication.parser;

import com.compass.deduplication.model.Contact;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CsvContactParser {

    static final String CONTACT_ID_HEADER = "contactID";
    static final String FIRST_NAME_HEADER = "name";
    static final String LAST_NAME_HEADER = "name1";
    static final String EMAIL_HEADER = "email";
    static final String ZIP_CODE_HEADER = "postalZip";
    static final String ADDRESS_HEADER = "address";
    private static final Set<String> REQUIRED_HEADERS = Set.of(
            CONTACT_ID_HEADER,
            FIRST_NAME_HEADER,
            LAST_NAME_HEADER,
            EMAIL_HEADER,
            ZIP_CODE_HEADER,
            ADDRESS_HEADER
    );

    public List<Contact> parse(Path csvPath) throws IOException {
        try (Reader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8);
             CSVParser csvParser = csvFormat().parse(reader)) {
            validateRequiredHeaders(csvParser);
            List<Contact> contacts = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                contacts.add(toContact(record));
            }
            return contacts;
        }
    }

    private CSVFormat csvFormat() {
        return CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build();
    }

    private void validateRequiredHeaders(CSVParser csvParser) {
        Set<String> missingHeaders = new LinkedHashSet<>(REQUIRED_HEADERS);
        missingHeaders.removeAll(csvParser.getHeaderMap().keySet());

        if (!missingHeaders.isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing required CSV headers: " + String.join(", ", missingHeaders)
            );
        }
    }

    private Contact toContact(CSVRecord record) {
        return new Contact(
                read(record, CONTACT_ID_HEADER),
                read(record, FIRST_NAME_HEADER),
                read(record, LAST_NAME_HEADER),
                read(record, EMAIL_HEADER),
                read(record, ZIP_CODE_HEADER),
                read(record, ADDRESS_HEADER)
        );
    }

    private String read(CSVRecord record, String headerName) {
        if (!record.isSet(headerName)) {
            return "";
        }

        String value = record.get(headerName);
        return value == null ? "" : value.trim();
    }
}
