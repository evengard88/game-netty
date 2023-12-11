package me.ikurenkov.game.logic.application.impl.handler;

import jakarta.inject.Inject;
import me.ikurenkov.game.logic.application.GameHandler;
import me.ikurenkov.game.logic.application.port.out.MessagePort;
import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.PlayerId;

public class WaitForOpponentHandler implements GameHandler {

    private final MessagePort messagePort;

    @Inject
    public WaitForOpponentHandler(MessagePort messagePort) {
        this.messagePort = messagePort;
    }


    public void handle(Game game, PlayerId id) {
        messagePort.sendMessage(id.getServerId(), id.getChannelId(), "No opponent available! Wait for opponent.");
    }

    @Override
    public void handle(Game game, PlayerId id, String message) {
        handle(game, id);
    }

    @Override
    public boolean supports(Game game, PlayerId id) {
        return game.getActor(id).requiresMove() && game.getOpponent(id) == null;
    }
}
