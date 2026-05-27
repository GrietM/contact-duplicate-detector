package com.compass.deduplication.matcher.rules;

import com.compass.deduplication.model.Contact;

public interface MatchingRule {

    int evaluate(Contact source, Contact candidate);

    String getRuleName();
}
