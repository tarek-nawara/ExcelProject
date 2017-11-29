package com.erricson.tttool.model.entities;

import java.util.Map;

public class Criteria {
    public Map<String, String> keysAndComments;

    public String findMatchingValue(final String key) {
        return keysAndComments.entrySet().stream()
                .filter(entry -> key.toLowerCase().contains(entry.getKey().toLowerCase()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse("");
    }
}
