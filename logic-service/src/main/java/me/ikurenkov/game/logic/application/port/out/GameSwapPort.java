package me.ikurenkov.game.logic.application.port.out;

import me.ikurenkov.game.logic.domain.PlayerId;

public interface GameSwapPort {
    void gameSwap(PlayerId id, String newGameId);
}
