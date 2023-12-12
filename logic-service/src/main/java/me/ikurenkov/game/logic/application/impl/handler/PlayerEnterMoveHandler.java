package me.ikurenkov.game.logic.application.impl.handler;


import com.google.inject.Inject;
import me.ikurenkov.game.logic.application.GameHandler;
import me.ikurenkov.game.logic.application.port.out.GameUpdatePort;
import me.ikurenkov.game.logic.application.port.out.MessagePort;
import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.Move;
import me.ikurenkov.game.logic.domain.PlayerId;

public class PlayerEnterMoveHandler implements GameHandler {
    private final GameHandler nextHandler;
    private final GameUpdatePort gameUpdatePort;
    private final MessagePort messagePort;

    @Inject
    public PlayerEnterMoveHandler(GameEvaluateHandler nextHandler, GameUpdatePort gameUpdatePort, MessagePort messagePort) {
        this.nextHandler = nextHandler;
        this.gameUpdatePort = gameUpdatePort;
        this.messagePort = messagePort;
    }

    @Override
    public void handle(Game game, PlayerId id, String message) {
        if (isValid(message)) {
            game.getActor(id).move(Move.findByNameOrValue(message));
            messagePort.sendMessage(id, STR."Your move is: \{game.getActor(id).getMove()}");
            gameUpdatePort.gameUpdate(game);
            nextHandler.handle(game, id, message);
        } else {
            messagePort.sendMessage(id, "Enter your move: rock(1), paper(2) or scissors(3)");
        }
    }

    @Override
    public boolean supports(Game game, PlayerId id) {
        return game.getOpponent(id) != null && game.getActor(id).requiresMove();
    }

    protected boolean isValid(String message) {
        return Move.findByNameOrValue(message) != null;
    }
}
