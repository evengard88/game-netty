package com.ikurenkov.game.application.handler;

import com.ikurenkov.game.Move;
import com.ikurenkov.game.application.GameContext;
import com.ikurenkov.game.application.GameHandler;
import com.ikurenkov.game.domain.Player;

public class BattleHandler implements GameHandler {
    @Override
    public void handle(GameContext context, String message) {
        if (isValid(context, message)) {
            context.getActor().ifPresent(a -> a.setMove(Move.findByNameOrValue(message)));
            context.getActor().ifPresent(a -> a.sendMassage("Enter your move: rock(1), paper(2) or scissors(3)"));

        } else {
            context.getActor().ifPresent(a -> a.sendMassage("Enter your move: rock(1), paper(2) or scissors(3)"));
        }
    }

    @Override
    public boolean supports(GameContext context) {
        return context.getActor()
                .map(Player::requiresMove)
                .orElse(false)
                && context.getGame().isPresent();
    }

    protected boolean isValid(GameContext context, String message) {
        return Move.findByNameOrValue(message) != null;
    }
}
