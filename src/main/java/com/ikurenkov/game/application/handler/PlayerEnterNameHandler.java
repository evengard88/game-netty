package com.ikurenkov.game.application.handler;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.ikurenkov.game.domain.Player;
import com.ikurenkov.game.application.GameContext;
import com.ikurenkov.game.application.GameHandler;

public class PlayerEnterNameHandler implements GameHandler {

    private final GameHandler nextHandler;

    @Inject
    public PlayerEnterNameHandler(GameHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handle(GameContext context, String message) {
        if (isValid(context, message)) {
            context.getActor().ifPresent(a -> a.setName(message));
            context.getActor().ifPresent(p->p.sendMassage("Welcome, " + p.getName() + "!"));
            nextHandler.handle(context, message);
        }
    }

    @Override
    public boolean supports(GameContext context) {
        return context.getActor().map(Player::requiresName).orElse(false);
    }

    protected boolean isValid(GameContext context, String message) {
        return !Strings.isNullOrEmpty(message);
    }
}
