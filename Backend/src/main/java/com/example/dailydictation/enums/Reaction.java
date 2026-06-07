package com.example.dailydictation.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Reaction {
    Like, Unlike;
    @JsonCreator
    public static Reaction fromString(String key) {
        return key == null ? null : Reaction.valueOf(key);
    }

    @JsonValue
    public String getValue() {
        return this.name();
    }
}
