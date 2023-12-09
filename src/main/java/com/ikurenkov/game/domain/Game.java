package com.ikurenkov.game.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Game {
    private final Player player1;
    private final Player player2;
}
