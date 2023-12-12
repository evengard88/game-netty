package me.ikurenkov.game.application.handler;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import me.ikurenkov.game.application.GameContext;
import me.ikurenkov.game.application.GameHandler;
import me.ikurenkov.game.domain.Game;
import me.ikurenkov.game.domain.Player;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameStartHandler implements GameHandler {
    private final BlockingQueue<GameContext> lobby;
    private final static Lock lock = new ReentrantLock();

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
                try {
                    lock.lock();
                    secondGameContext = lobby.poll();
                    if (secondGameContext == null) {
                        lobby.add(context);
                        actor.sendMassage("No opponent available! Wait for opponent.");
                    }
                } finally {
                    lock.unlock();
                }
            }

            if (secondGameContext != null) {
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
