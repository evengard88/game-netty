package me.ikurenkov.game.application.handler;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import me.ikurenkov.game.application.GameContext;
import me.ikurenkov.game.application.GameHandler;

public class PlayerEnterNameHandler implements GameHandler {

    private final GameHandler nextHandler;

    @Inject
    public PlayerEnterNameHandler(GameHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handle(GameContext context, String message) {
        if (isValid(message)) {
            context.getActor().setName(message);
            context.getActor().sendMassage("Welcome, " + context.getActor().getName() + "!");
            nextHandler.handle(context, message);
        }
    }

    @Override
    public boolean supports(GameContext context) {
        return context.getActor().requiresName();
    }

    protected boolean isValid(String message) {
        return !Strings.isNullOrEmpty(message);
    }
}
