package com.compass.deduplication.parser;

import com.compass.deduplication.model.Contact;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvContactParserTest {

    private final CsvContactParser parser = new CsvContactParser();

    @Test
    void shouldParseContactsUsingAssessmentHeaders() throws IOException, URISyntaxException {
        List<Contact> contacts = parser.parse(testResourcePath("contacts-test.csv"));

        assertEquals(3, contacts.size());

        Contact firstContact = contacts.get(0);
        assertEquals("1", firstContact.contactId());
        assertEquals("Ciara", firstContact.firstName());
        assertEquals("French", firstContact.lastName());
        assertEquals("mollis.lectus.pede@outlook.net", firstContact.email());
        assertEquals("39746", firstContact.zipCode());
        assertEquals("449-6990 Tellus. Rd.", firstContact.address());
    }

    @Test
    void shouldIgnoreBlankLines() throws IOException, URISyntaxException {
        List<Contact> contacts = parser.parse(testResourcePath("contacts-test.csv"));

        assertEquals(List.of("1", "2", "3"), contacts.stream().map(Contact::contactId).toList());
    }

    @Test
    void shouldDefaultMissingFieldsToEmptyStrings() throws IOException, URISyntaxException {
        List<Contact> contacts = parser.parse(testResourcePath("contacts-test.csv"));

        Contact thirdContact = contacts.get(2);
        assertEquals("3", thirdContact.contactId());
        assertEquals("Victor", thirdContact.firstName());
        assertEquals("", thirdContact.lastName());
        assertEquals("", thirdContact.email());
        assertEquals("82025", thirdContact.zipCode());
        assertEquals("", thirdContact.address());
    }

    @Test
    void shouldFailWhenRequiredHeaderIsMissing() throws IOException {
        Path tempFile = Files.createTempFile("contacts-missing-header", ".csv");
        Files.writeString(tempFile, """
                contactID,name,name1,email,postalZip
                1,Ciara,French,mollis.lectus.pede@outlook.net,39746
                """);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(tempFile));

        assertEquals("Missing required CSV headers: address", exception.getMessage());
    }

    private Path testResourcePath(String resourceName) throws URISyntaxException {
        return Path.of(getClass().getClassLoader().getResource(resourceName).toURI());
    }
}
