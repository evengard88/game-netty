package me.ikurenkov.game.logic.application;

import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.PlayerId;

public interface GameHandler {
    void handle(Game game, PlayerId id, String message);

    boolean supports(Game game, PlayerId id);

}

