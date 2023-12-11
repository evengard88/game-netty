package me.ikurenkov.game.application;

import me.ikurenkov.game.application.GameContext;

public interface GameHandler {
    void handle(GameContext context, String message);
    boolean supports(GameContext context);
}

