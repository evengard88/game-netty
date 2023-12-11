package me.ikurenkov.game.application.impl;

import com.google.inject.Inject;
import me.ikurenkov.game.application.GameContext;
import me.ikurenkov.game.application.GameHandler;
import me.ikurenkov.game.application.GameService;
import me.ikurenkov.game.application.PlayerMessagePort;
import me.ikurenkov.game.domain.Player;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

@Log
public class GameServiceImpl implements GameService {

    private final Set<GameHandler> gameHandlers;
    private final BlockingQueue<GameContext> lobby;

    @Inject
    public GameServiceImpl(Set<GameHandler> gameHandlers, BlockingQueue<GameContext> lobby) {
        this.gameHandlers = gameHandlers;
        this.lobby = lobby;
    }

    @Override
    public void start(GameContext context, PlayerMessagePort actorPort) {
        Player player = new Player(actorPort);
        context.setActor(player);
        player.sendMassage("Hello! Enter your name, at least one symbol");
    }

    @Override
    public void handleMessage(GameContext context, String message) {
        List<GameHandler> availableHandlers = gameHandlers.stream()
                .filter(gh -> gh.supports(context))
                .toList();
        if (availableHandlers.size() != 1) {
            log.severe("Incorrect number of handlers" + availableHandlers);
        }
        if (!availableHandlers.isEmpty()) {
            availableHandlers.getFirst().handle(context, message);
        }

    }

    @Override
    public void disconnect(GameContext context) {
        lobby.remove(context);
        Optional.of(context)
                .map(GameContext::getGame)
                .map(g -> g.getOpponent(context.getActor()))
                .ifPresent(p -> p.sendMassage("Your opponent left! You won!"));
    }
}
