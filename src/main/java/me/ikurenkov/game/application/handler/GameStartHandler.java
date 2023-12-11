package me.ikurenkov.game.application.handler;

import com.google.inject.Inject;
import me.ikurenkov.game.application.GameContext;
import me.ikurenkov.game.application.GameHandler;
import me.ikurenkov.game.domain.Game;
import me.ikurenkov.game.domain.Player;
import lombok.SneakyThrows;

import java.util.concurrent.BlockingQueue;

public class GameStartHandler implements GameHandler {
    private final BlockingQueue<GameContext> lobby;

    @Inject
    public GameStartHandler(BlockingQueue<GameContext> lobby) {
        this.lobby = lobby;
    }

    @SneakyThrows
    @Override
    public void handle(GameContext context, String message) {
        if (supports(context)) {
            Player actor = context.getActor();
            actor.sendMassage("Searching for opponent...");
            GameContext secondGameContext = lobby.poll();
            if (secondGameContext == null) {
                lobby.add(context);
                actor.sendMassage("No opponent available! Wait for opponent.");
            } else {
                Player secondPlayer = secondGameContext.getActor();
                Game game = new Game(actor, secondPlayer);
                context.setGame(game);
                secondGameContext.setGame(game);

                actor.sendMassage("Your opponent is " + secondPlayer.getName() + "!");
                secondPlayer.sendMassage("Your opponent is " + actor.getName() + "!");

                actor.sendMassage("Enter your move: rock(1), paper(2) or scissors(3)");
                secondPlayer.sendMassage("Enter your move: rock(1), paper(2) or scissors(3)");
            }
        }
    }

    @Override
    public boolean supports(GameContext context) {
        return context.getGame() == null;
    }

}
