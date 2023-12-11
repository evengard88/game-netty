package me.ikurenkov.game.application.handler;

import me.ikurenkov.game.application.GameContext;
import me.ikurenkov.game.application.GameHandler;

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
