package com.ikurenkov.game.application;

import com.ikurenkov.game.application.GameContext;

public interface GameHandler {
    void handle(GameContext context, String message);
    boolean supports(GameContext context);
}

