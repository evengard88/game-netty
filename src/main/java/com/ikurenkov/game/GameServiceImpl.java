package com.ikurenkov.game;

import com.google.inject.Inject;
import com.ikurenkov.game.application.GameContext;
import com.ikurenkov.game.application.GameHandler;
import com.ikurenkov.game.application.GameService;
import com.ikurenkov.game.application.PlayerMessagePort;
import com.ikurenkov.game.domain.Player;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log
public class GameServiceImpl implements GameService {

    private final Set<GameHandler> gameHandlers;

    @Inject
    public GameServiceImpl(Set<GameHandler> gameHandlers) {
        this.gameHandlers = gameHandlers;
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
        if (availableHandlers.size() > 0) {
            availableHandlers.get(0).handle(context, message);
        }

    }

    @Override
    public void disconnect(GameContext context) {
        Optional.ofNullable(context)
                .flatMap(GameContext::getGame)
                .map(g -> g.getOpponent(context.getActor().orElse(null)))
                .ifPresent(p -> p.sendMassage("Your opponent left! You won!"));
    }
}
