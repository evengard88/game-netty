package me.ikurenkov.game.logic.application.port.in;

import me.ikurenkov.game.logic.domain.Player;

public interface GameStartUseCase {
    void startGame(Player p1, Player p2);
}
