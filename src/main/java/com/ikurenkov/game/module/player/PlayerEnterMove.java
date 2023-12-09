package com.ikurenkov.game.module.player;

import com.ikurenkov.game.Move;
import com.ikurenkov.game.module.GameContext;
import com.ikurenkov.game.module.AbstractGameHandler;


public class PlayerEnterMove extends AbstractGameHandler {
    @Override
    public String handle(GameContext context, String message) {
        if (isValid(context, message)) {
            context.getActor().setMove(Move.findByNameOrValue(message));
            return "";
        } else {return "";}
    }

    protected boolean isValid(GameContext context, String message) {
        return Move.findByNameOrValue(message) != null;
    }
}
