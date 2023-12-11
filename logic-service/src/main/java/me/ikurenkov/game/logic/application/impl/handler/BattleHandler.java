package me.ikurenkov.game.logic.application.impl.handler;

import jakarta.inject.Inject;
import me.ikurenkov.game.logic.application.GameHandler;
import me.ikurenkov.game.logic.application.port.out.MessagePort;
import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.Move;
import me.ikurenkov.game.logic.domain.PlayerId;

//@ApplicationScoped
public class BattleHandler implements GameHandler {

    @Inject
    private MessagePort messagePort;

    @Override
    public void handle(Game game, PlayerId id, String message) {
        if (isValid(message)) {
            game.getActor(id).move(Move.findByNameOrValue(message));
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
