package com.ikurenkov.game.application.handler;

import com.ikurenkov.game.application.GameContext;
import com.ikurenkov.game.application.GameHandler;

public class WaitForOpponentHandler implements GameHandler {

    @Override
    public void handle(GameContext context, String message) {
        context.getActor().sendMassage("No opponent available! Wait for opponent.");
    }

    @Override
    public boolean supports(GameContext context) {
        return context.getActor().requiresMove() && context.getGame() == null;
    }
}
