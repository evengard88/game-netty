package me.ikurenkov.game.logic.application.impl.handler;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import me.ikurenkov.game.logic.application.GameHandler;
import me.ikurenkov.game.logic.application.port.out.GameUpdatePort;
import me.ikurenkov.game.logic.application.port.out.MessagePort;
import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.PlayerId;

public class PlayerEnterNameHandler implements GameHandler {
    private final GameHandler nextHandler;
    private final GameUpdatePort gameUpdatePort;

    @Inject
    public PlayerEnterNameHandler(WaitForOpponentHandler nextHandler, GameUpdatePort gameUpdatePort, MessagePort messagePort) {
        this.nextHandler = nextHandler;
        this.gameUpdatePort = gameUpdatePort;
        this.messagePort = messagePort;
    }

    private final MessagePort messagePort;

    @Override
    public void handle(Game game, PlayerId id, String message) {
        if (isValid(message)) {
            game.getActor(id).name(message);
            gameUpdatePort.gameUpdate(game);
            messagePort.sendMessage(id, STR."Welcome, \{game.getActor(id).getName()}!");
            nextHandler.handle(game, id, message);
        }
        else {
            messagePort.sendMessage(id,"Enter your name, at least one symbol");
        }
    }

    @Override
    public boolean supports(Game game, PlayerId id) {
        return game.getActor(id).requiresName();
    }

    protected boolean isValid(String message) {
        return !Strings.isNullOrEmpty(message);
    }
}
