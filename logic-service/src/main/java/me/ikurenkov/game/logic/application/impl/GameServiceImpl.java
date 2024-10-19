package me.ikurenkov.game.logic.application.impl;


import io.quarkus.arc.All;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.java.Log;
import me.ikurenkov.game.logic.application.GameHandler;
import me.ikurenkov.game.logic.application.PlayerHandler;
import me.ikurenkov.game.logic.application.port.in.GameStartUseCase;
import me.ikurenkov.game.logic.application.port.in.LobbyStorePort;
import me.ikurenkov.game.logic.application.port.in.MessageReceivedUseCase;
import me.ikurenkov.game.logic.application.port.in.PlayerDisconnectsUseCase;
import me.ikurenkov.game.logic.application.port.out.*;
import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.Player;
import me.ikurenkov.game.logic.domain.PlayerId;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.github.siahsang.redutils.RedUtilsLock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Log
@ApplicationScoped
public class GameServiceImpl implements MessageReceivedUseCase, PlayerDisconnectsUseCase, GameStartUseCase {

    @Inject
    @All
    private List<GameHandler> gameHandlers;
    @Inject
    @All
    private List<PlayerHandler> playerHandlers;

    @Inject
    private MessagePort messagePort;
    @Inject
    private DisconnectPort disconnectPort;
    @Inject
    private PlayerDeletePort playerDeletePort;

    @Inject
    private GameDeletePort gameDeletePort;
    @Inject
    private GameGetPort gameGetPort;
    @Inject
    private GameStorePort gameStorePort;
    @Inject
    private PlayerStorePort playerStorePort;
    @Inject
    private PlayerGetPort getPlayerPort;
    @Inject
    private LobbyStorePort lobbyStorePort;
    @Inject
    RedUtilsLock redUtilsLock;

    @Override
    @Retry(maxRetries = 4)
    public void handleMessage(String serverId, String channelId, String message) {
        PlayerId playerId = new PlayerId(serverId, channelId);
        Player player = getPlayerPort.getOrCreate(playerId);
        redUtilsLock.acquire(player.getPlayerId().toString(), () -> {
            processPlayerActions(player, message);
            Optional.ofNullable(player.getGameId())
                    .map(gameGetPort::getById)
                    .ifPresent(game ->
                            redUtilsLock.acquire(
                                    game.getGameId(),
                                    () -> processGameActions(game, playerId, message)));

        });
    }

    private void processGameActions(Game game, PlayerId playerId, String message) {
        List<GameHandler> gameAvailableHandlers = gameHandlers
                .stream()
                .filter(gh -> gh.supports(game, playerId))
                .toList();
        if (gameAvailableHandlers.size() != 1) {
            log.severe(STR."Incorrect number of handlers\{gameAvailableHandlers}");
        }
        if (!gameAvailableHandlers.isEmpty()) {
            gameAvailableHandlers.getFirst().handle(game, playerId, message);
        }
    }

    private void processPlayerActions(Player player, String message) {
        List<PlayerHandler> playerAvailableHandlers = playerHandlers
                .stream()
                .filter(gh -> gh.supports(player))
                .toList();

        if (playerAvailableHandlers.size() != 1) {
            log.severe(STR."Incorrect number of handlers\{playerAvailableHandlers}");
        }
        if (!playerAvailableHandlers.isEmpty()) {
            playerAvailableHandlers.getFirst().handle(player, message);
        }
    }

    @Override
    public void playerDisconnect(String serverId, String channelId) {
        PlayerId playerId = new PlayerId(serverId, channelId);
        Player player = getPlayerPort.getOrCreate(playerId);
        playerDeletePort.delete(player);
        lobbyStorePort.remove(player);
        if (player.getGameId() == null) {
            return;
        }
        Game game = gameGetPort.getById(player.getGameId());
        if (game != null) {
            Player opponent = game.getOpponent(player);
            disconnectPort.disconnect(opponent.getPlayerId(), "Your opponent left! You won!");
            playerDeletePort.delete(opponent);
            gameDeletePort.delete(game);
        }
    }

    @Override
    @Retry(maxRetries = 4)
    public void startGame(Player p1, Player p2) {
        String player1Id = p1.getPlayerId().toString();
        String player2Id = p2.getPlayerId().toString();
        String key = (player1Id.compareTo(player2Id) > 0) ? player1Id : player2Id;
        redUtilsLock.acquire(key, () -> {
            String gameId = UUID.randomUUID().toString();
            p1.setGameId(gameId);
            p2.setGameId(gameId);
            playerStorePort.store(p1);
            playerStorePort.store(p2);
            gameStorePort.store(new Game().setGameId(gameId).setPlayer1(p1).setPlayer2(p2));
            messagePort.sendMessage(p1.getPlayerId(), STR."Your opponent is \{p2.getName()}!");
            messagePort.sendMessage(p2.getPlayerId(), STR."Your opponent is \{p1.getName()}!");

            messagePort.sendMessage(p1.getPlayerId(), "Enter your move: rock(1), paper(2) or scissors(3)");
            messagePort.sendMessage(p2.getPlayerId(), "Enter your move: rock(1), paper(2) or scissors(3)");
        });
    }

}
