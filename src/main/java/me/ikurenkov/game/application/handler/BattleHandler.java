package me.ikurenkov.game.application.handler;

import me.ikurenkov.game.domain.Move;
import me.ikurenkov.game.application.GameContext;
import me.ikurenkov.game.application.GameHandler;

public class BattleHandler implements GameHandler {
    @Override
    public void handle(GameContext context, String message) {
        if (isValid(message)) {
            context.getActor().setMove(Move.findByNameOrValue(message));
            context.getActor().sendMassage("Enter your move: rock(1), paper(2) or scissors(3)");

        } else {
            context.getActor().sendMassage("Enter your move: rock(1), paper(2) or scissors(3)");
        }
    }

    @Override
    public boolean supports(GameContext context) {
        return context.getActor().requiresMove() && context.getGame() != null;
    }

    protected boolean isValid(String message) {
        return Move.findByNameOrValue(message) != null;
    }
}
