package me.ikurenkov.game.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Data
@RequiredArgsConstructor
public class Game {
    private final Player player1;
    private final Player player2;

    public final Player getOpponent(Player actor) {
        return (actor == player1) ? player2 : player1;
    }
}
