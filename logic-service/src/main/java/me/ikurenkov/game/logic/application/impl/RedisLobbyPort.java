package me.ikurenkov.game.logic.application.impl;

import com.google.gson.Gson;
import com.google.inject.Inject;
import io.lettuce.core.api.StatefulRedisConnection;
import me.ikurenkov.game.logic.application.port.in.LobbyPlayerStorePort;
import me.ikurenkov.game.logic.application.port.out.LobbyPlayerRemovePort;
import me.ikurenkov.game.logic.configuration.LettuceModule;
import me.ikurenkov.game.logic.domain.PlayerId;

public class RedisLobbyPort implements LobbyPlayerRemovePort, LobbyPlayerStorePort {
    private final StatefulRedisConnection<String, String> connection;
    private final Gson gson;

    @Inject
    public RedisLobbyPort(@LettuceModule.DefaultConnection StatefulRedisConnection connection, Gson gson) {
        this.connection = connection;
        this.gson = gson;
    }

    @Override
    public void store(PlayerId p) {
        connection.sync().lpush("lobby", gson.toJson(p));
    }

    @Override
    public void remove(PlayerId p) {
        connection.sync().lrem("lobby", 1, gson.toJson(p));
    }

    @Override
    public PlayerId removeAndGetAny() {
        String s = connection.sync().lpop("lobby");
        return gson.fromJson(s, PlayerId.class);
    }
}
