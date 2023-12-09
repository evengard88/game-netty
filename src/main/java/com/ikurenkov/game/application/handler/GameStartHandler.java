package com.ikurenkov.game.application.handler;

import com.google.inject.Inject;
import com.ikurenkov.game.application.GameContext;
import com.ikurenkov.game.application.GameHandler;
import com.ikurenkov.game.domain.Game;
import com.ikurenkov.game.domain.Player;
import lombok.SneakyThrows;

import java.util.concurrent.BlockingQueue;

public class GameStartHandler implements GameHandler {
    private final BlockingQueue<Player> lobby;

    @Inject
    public GameStartHandler(BlockingQueue<Player> lobby) {
        this.lobby = lobby;
    }

    @SneakyThrows
    @Override
    public void handle(GameContext context, String message) {
        if (supports(context)) {
            context.getActor().ifPresent(p -> p.sendMassage("Searching for opponent..."));
            Player actor = context.getActor().get();
            Player secondPlayer = lobby.poll();
            if (secondPlayer == null) {
                lobby.add(actor);
                actor.sendMassage("No opponent available! Wait for opponent.");
            } else {
                context.setGame(new Game(actor, secondPlayer));

                actor.sendMassage("Your opponent is " + secondPlayer.getName() + "!");
                secondPlayer.sendMassage("Your opponent is " + actor.getName() + "!");

                actor.sendMassage("Enter your move: rock(1), paper(2) or scissors(3)");
                secondPlayer.sendMassage("Enter your move: rock(1), paper(2) or scissors(3)");
            }
        }
    }

    @Override
    public boolean supports(GameContext context) {
        return context.getActor().isPresent() && context.getGame().isEmpty();
    }

}
