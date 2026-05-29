# Contact Duplicate Detector

## Project Overview

Contact Duplicate Detector is a command-line application that identifies potential duplicate contacts from a CSV file.

The solution loads contacts into memory, evaluates multiple matching signals such as email, name, address and zip code, calculates a score based on matching evidence and classifies potential duplicates into three confidence levels:

- HIGH
- MEDIUM
- LOW

The application generates a CSV file containing all detected matches and their associated confidence level.

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
- [Output Format](#output-format)
- [Matching Strategy](#matching-strategy)
- [Confidence Classification](#confidence-classification)
- [Architecture and Design](#architecture-and-design)
- [Technical Decisions](#technical-decisions)
- [Testing and Coverage](#testing-and-coverage)
- [Dependencies](#dependencies)
- [Possible Improvements](#possible-improvements)

---

## Prerequisites

- Java 17+
- Maven 3.8+

---

## Building the Project

Run all commands from the project root.

Build the executable jar:

```bash
mvn clean package
```

Run all tests:

```bash
mvn test
```

Generate the coverage report:

```bash
mvn clean verify
```

Coverage report location:

```text
target/site/jacoco/index.html
```

---

## Running the Application

### Using the bundled sample dataset

The repository includes a sample input file:

```text
src/main/resources/Contacts.csv
```

Run the application:

```bash
java -jar target/contact-duplicate-detector-1.0-SNAPSHOT.jar src/main/resources/Contacts.csv
```

Example console output:

```text
Loaded contacts: 1000

Matches written: 1223
  HIGH:   463
  MEDIUM: 652
  LOW:    108

Output file: output/results.csv
```

Using the bundled sample dataset, the application produces 1,223 potential duplicate matches.

### Custom output location

```bash
java -jar target/contact-duplicate-detector-1.0-SNAPSHOT.jar src/main/resources/Contacts.csv output/custom-results.csv
```

If no output path is provided, the application writes the result to:

```text
output/results.csv
```

---

## Output Format

The generated CSV contains the following columns:

```csv
source_contact_id,match_contact_id,accuracy,score
```

Example:

```csv
source_contact_id,match_contact_id,accuracy,score
1,501,HIGH,100
2,502,HIGH,100
3,503,MEDIUM,60
```

Where:

- `source_contact_id` = original contact
- `match_contact_id` = detected duplicate candidate
- `accuracy` = HIGH / MEDIUM / LOW
- `score` = final score after score capping

The CSV column is named "accuracy" as required by the assessment output contract and represents the final confidence level (HIGH, MEDIUM or LOW).

---

## Matching Strategy

The solution evaluates several independent matching rules.

| Rule | Score |
|---|---:|
| Exact Email | 100 |
| Email Username | 60 |
| Address | 45 |
| Last Name | 25 |
| First Name | 20 |
| Initial Name | 15 |
| Zip Code | 15 |

Each rule contributes evidence independently.

The accumulated score is capped at 100.

### Strategy Pattern

Matching rules are implemented using the **Strategy Pattern**.

Each rule encapsulates a single matching criterion and implements the common `MatchingRule` contract.

Examples:

- `ExactEmailRule`
- `EmailUsernameRule`
- `FirstNameRule`
- `LastNameRule`
- `AddressRule`
- `ZipCodeRule`

New matching rules can be added without modifying the matcher orchestration.

---

## Confidence Classification

Confidence is not determined exclusively by score.

The solution applies business guardrails before threshold-based classification.

### HIGH

Examples:

- Exact email match
- Email username + address
- Address + last name
- Address + full name
- Email username + full name + zip code

### MEDIUM

Examples:

- Email username only
- Address only
- Full name only
- Email username + full name

### LOW

Examples:

- Last name + zip code
- Other weak combinations with sufficient score

This approach prevents weak combinations from being promoted solely because they accumulate points.

---

## Architecture and Design

The application follows a simple layered design:

```text
CSV Input
    |
    v
CsvContactParser
    |
    v
ContactMatcher
    |
    +--> Matching Rules
    |
    +--> ScoreCalculator
    |
    +--> ConfidenceClassifier
    |
    v
CsvMatchWriter
    |
    v
CSV Output
```

### Main Components

#### CsvContactParser

Reads the CSV input and converts rows into `Contact` records.

#### StringNormalizer

Provides generic text normalization:

- trim
- lowercase conversion
- punctuation handling
- whitespace normalization

#### Matching Rules

Independent strategies that evaluate specific matching signals.

#### ScoreCalculator

Aggregates matching evidence and caps the final score at 100.

#### ConfidenceClassifier

Applies business guardrails and determines the final confidence level.

#### ContactMatcher

Coordinates pair comparison and duplicate detection.

#### CsvMatchWriter

Produces the final CSV output sorted by score.

---

## Technical Decisions

### Why no Spring Boot?

This challenge is fundamentally a command-line batch process.

Using Spring Boot would introduce additional complexity without providing significant value for the problem being solved.

Manual dependency wiring keeps the solution:

- lightweight
- explicit
- easy to understand
- easy to review

### SOLID Principles Applied

The design intentionally follows several SOLID principles.

#### Single Responsibility Principle

Each component has a focused responsibility:

- parser
- matcher
- scorer
- classifier
- writer

#### Open/Closed Principle

New matching rules can be added by implementing `MatchingRule` without changing `ContactMatcher`.

#### Dependency Injection

Dependencies are provided through constructors rather than being created internally.

This improves:

- testability
- flexibility
- separation of concerns

#### Strategy Pattern

Matching logic is implemented through the MatchingRule interface and its concrete implementations, allowing new matching criteria to be introduced without modifying existing orchestration logic.

### Why O(n²)?

The application compares each contact pair exactly once.

For the provided dataset of 1,000 contacts, this results in approximately:

```text
1000 * 999 / 2 = 499,500 comparisons
```

This is acceptable for an in-memory coding assessment.

I intentionally prioritized simplicity, readability and deterministic behavior over premature optimization.

For significantly larger datasets, blocking or indexing strategies could be introduced to reduce the candidate search space before applying matching rules.

### Why score + guardrails?

A score alone is often insufficient to determine confidence.

Some weak signals may accumulate a high score, while some strong signals should immediately indicate a highly probable match.

Business guardrails allow the classifier to prioritize stronger evidence while avoiding misleading classifications.

### Why immutable records?

The Contact domain model is implemented using Java records.

This provides:
- immutability
- concise code
- safer data handling
- value-based semantics

---

## Testing and Coverage

The project contains automated unit tests covering:

- CSV parsing
- Normalization
- Matching rules
- Scoring
- Classification
- Matching orchestration
- CSV writing

Run tests:

```bash
mvn test
```

Generate coverage report:

```bash
mvn clean verify
```

Coverage report:

```text
target/site/jacoco/index.html
```

Current JaCoCo summary:

| Metric | Coverage |
|---|---:|
| Instruction coverage | 81% |
| Branch coverage | 82% |

---

## Dependencies

### Apache Commons CSV

Used for:

- CSV parsing
- CSV generation

### JUnit 5

Used for:

- Unit testing

### JaCoCo

Used for:

- Coverage reporting

### Maven Shade Plugin

Used for:

- Creating an executable fat jar with all required dependencies

---

## Possible Improvements

For larger datasets or future iterations, the following improvements could be considered:

- Blocking/indexing to reduce O(n²) comparisons
- More advanced fuzzy matching strategies for names and addresses
- Externalized scoring and confidence configuration
- Additional output metadata showing which rules matched
