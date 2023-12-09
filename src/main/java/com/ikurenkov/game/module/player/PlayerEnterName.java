package com.ikurenkov.game.module.player;

import com.google.common.base.Strings;
import com.ikurenkov.game.module.GameContext;
import com.ikurenkov.game.module.AbstractGameHandler;

public class PlayerEnterName extends AbstractGameHandler {
    @Override
    public String handle(GameContext context, String message) {
        if (isValid(context, message)) {
            context.getActor().setName(message);
            return "name "+ message;
        }
        else return "";
    }

    protected boolean isValid(GameContext context, String message) {
        return !Strings.isNullOrEmpty(message);
    }
}
