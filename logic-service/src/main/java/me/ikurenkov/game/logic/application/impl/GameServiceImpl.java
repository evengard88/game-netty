package me.ikurenkov.game.logic.application.impl;

import com.google.inject.Inject;
import lombok.extern.java.Log;
import me.ikurenkov.game.logic.application.GameHandler;
import me.ikurenkov.game.logic.application.port.in.LobbyPlayerStorePort;
import me.ikurenkov.game.logic.application.port.in.MessageReceivedUseCase;
import me.ikurenkov.game.logic.application.port.in.PlayerDisconnectsUseCase;
import me.ikurenkov.game.logic.application.port.in.PlayerInitializedUseCase;
import me.ikurenkov.game.logic.application.port.out.*;
import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.Player;
import me.ikurenkov.game.logic.domain.PlayerId;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Log
public class GameServiceImpl implements MessageReceivedUseCase, PlayerInitializedUseCase, PlayerDisconnectsUseCase {

    private final Set<GameHandler> gameHandlers;
    private final MessagePort messagePort;
    private final DisconnectPort disconnectPort;
    private final LobbyPlayerStorePort storePlayerPort;
    private final GameGetPort gameGetPort;
    private final GameCreatePort gameCreatePort;
    private final GameUpdatePort gameUpdatePort;

    @Inject
    public GameServiceImpl(Set<GameHandler> gameHandlers,
                           MessagePort messagePort,
                           DisconnectPort disconnectPort,
                           LobbyPlayerStorePort storePlayerPort,
                           GameGetPort gameGetPort,
                           GameCreatePort gameCreatePort,
                           GameUpdatePort gameUpdatePort) {
        this.gameHandlers = gameHandlers;
        this.messagePort = messagePort;
        this.disconnectPort = disconnectPort;
        this.storePlayerPort = storePlayerPort;
        this.gameGetPort = gameGetPort;
        this.gameCreatePort = gameCreatePort;
        this.gameUpdatePort = gameUpdatePort;
    }

    @Override
    public void playerInitialize(String serverId, String channelId) {
        Game game = new Game().setGameId(UUID.randomUUID().toString())
                .setPlayer1(new Player().setPlayerId(new PlayerId(serverId, channelId)));
        gameCreatePort.gameCreate(game);

        messagePort.sendMessage(serverId, channelId, "Hello! Enter your name, at least one symbol");
    }

    @Override
    public void handleMessage(String serverId, String channelId, String message) {
        PlayerId playerId = new PlayerId(serverId, channelId);
        Game game = gameGetPort.get(playerId);
        List<GameHandler> availableHandlers = gameHandlers.stream()
                .filter(gh -> gh.supports(game, playerId))
                .toList();
        if (availableHandlers.size() != 1) {
            log.severe(STR."Incorrect number of handlers\{availableHandlers}");
        }
        if (!availableHandlers.isEmpty()) {
            availableHandlers.getFirst().handle(game, playerId, message);
        }

    }

    @Override
    public void playerDisconnect(String serverId, String channelId) {
//        PlayerId playerId = new PlayerId(serverId, channelId);
//        storePlayerPort.remove(playerId);
//        gameGetPort.get(playerId);
    }
}
