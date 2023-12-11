package me.ikurenkov.game.logic.domain;

import java.util.Objects;

public enum Move {
    ROCK("1"),
    PAPER("2"),
    SCISSORS("3");

    private final String value;

    Move(String value) {
        this.value = value;
    }

    public static Move findByNameOrValue(String s) {
        for (Move m : values()) {
            if (Objects.equals(s, m.value) || Objects.equals(s.toLowerCase(), m.name().toLowerCase())) {
                return m;
            }
        }
        return null;
    }
}
