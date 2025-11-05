package ua.com.vladyslav.spribe.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.concurrent.ThreadLocalRandom;

public enum Genders {
    MALE,
    FEMALE,
    UNKNOWN;

    @JsonCreator
    public static Genders fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return Genders.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }

    public static Genders getRandom() {
        Genders[] valid = {MALE, FEMALE};
        int index = ThreadLocalRandom.current().nextInt(valid.length);
        return valid[index];
    }
}
