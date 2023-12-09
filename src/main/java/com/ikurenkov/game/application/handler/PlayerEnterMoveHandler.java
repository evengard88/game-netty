package com.ikurenkov.game.application.handler;

import com.google.inject.Inject;
import com.ikurenkov.game.Move;
import com.ikurenkov.game.application.GameContext;
import com.ikurenkov.game.application.GameHandler;

public class PlayerEnterMoveHandler implements GameHandler {
    private final GameHandler nextHandler;

    @Inject
    public PlayerEnterMoveHandler(GameEvaluateHandler gameEvaluateHandler) {
        this.nextHandler = gameEvaluateHandler;
    }

    @Override
    public void handle(GameContext context, String message) {
        if (isValid(message)) {
            context.getActor().setMove(Move.findByNameOrValue(message));
            context.getActor().sendMassage("Your move is: " + context.getActor().getMove());
            nextHandler.handle(context, message);
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
