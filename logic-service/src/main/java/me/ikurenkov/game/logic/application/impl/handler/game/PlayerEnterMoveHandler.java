package me.ikurenkov.game.logic.application.impl.handler.game;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.ikurenkov.game.logic.application.GameHandler;
import me.ikurenkov.game.logic.application.port.out.GameStorePort;
import me.ikurenkov.game.logic.application.port.out.MessagePort;
import me.ikurenkov.game.logic.application.port.out.PlayerStorePort;
import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.Move;
import me.ikurenkov.game.logic.domain.PlayerId;

@ApplicationScoped
public class PlayerEnterMoveHandler implements GameHandler {
    @Inject
    GameEvaluateHandler nextHandler;
    @Inject
    MessagePort messagePort;
    @Inject
    GameStorePort gameStorePort;
    @Inject
    PlayerStorePort playerStorePort;

    @Override
    public void handle(Game game, PlayerId id, String message) {
        if (isValid(message)) {
            game.getActor(id).move(Move.findByNameOrValue(message));
            messagePort.sendMessage(id, STR."Your move is: \{game.getActor(id).getMove()}");
            gameStorePort.store(game);
            playerStorePort.store(game.getActor(id));
            nextHandler.handle(game, id, message);
        } else {
            messagePort.sendMessage(id, "Enter your move: rock(1), paper(2) or scissors(3)");
        }
    }

    @Override
    public boolean supports(Game game, PlayerId id) {
        return game != null && game.getActor(id).requiresMove();
    }

    protected boolean isValid(String message) {
        return Move.findByNameOrValue(message) != null;
    }
}
