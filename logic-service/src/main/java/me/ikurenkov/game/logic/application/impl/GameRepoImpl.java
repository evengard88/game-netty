package me.ikurenkov.game.logic.application.impl;

import com.google.gson.Gson;
import com.google.inject.Inject;
import io.lettuce.core.api.StatefulRedisConnection;
import me.ikurenkov.game.logic.application.port.out.GameCreatePort;
import me.ikurenkov.game.logic.application.port.out.GameGetPort;
import me.ikurenkov.game.logic.application.port.out.GameUpdatePort;
import me.ikurenkov.game.logic.configuration.LettuceModule;
import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.PlayerId;

public class GameRepoImpl implements GameGetPort, GameCreatePort, GameUpdatePort {

    private final StatefulRedisConnection<String, String> connection;
    private final Gson gson;

    @Inject
    public GameRepoImpl(@LettuceModule.DefaultConnection StatefulRedisConnection connection, Gson gson) {
        this.connection = connection;
        this.gson = gson;
    }

    @Override
    public Game get(PlayerId playerId) {
        String gameId = connection.sync().hget("player2game", playerId.toString());
        String gameJson = connection.sync().hget("games", gameId);
        return gson.fromJson(gameJson, Game.class);
    }

    @Override
    public Game get(String gameId) {
        String gameJson = connection.sync().hget("games", gameId);
        return gson.fromJson(gameJson, Game.class);
    }

    @Override
    public Game gameCreate(Game game) {
        connection.sync().hset("player2game", game.getPlayer1().getPlayerId().toString(), game.getGameId());
        connection.sync().hset("games", game.getGameId(), gson.toJson(game));
        return game;
    }

    @Override
    public Game gameUpdate(Game game) {
        connection.sync().hset("games", game.getGameId(), gson.toJson(game));
        return game;
    }
}

