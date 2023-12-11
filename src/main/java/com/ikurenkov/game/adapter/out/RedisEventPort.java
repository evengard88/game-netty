package com.ikurenkov.game.adapter.out;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.ikurenkov.game.application.EventPort;
import com.ikurenkov.game.configuration.AppModule;
import com.ikurenkov.game.configuration.LettuceModule;
import com.ikurenkov.game.domain.DisconnectEvent;
import com.ikurenkov.game.domain.InitEvent;
import com.ikurenkov.game.domain.MessageEvent;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisEventPort implements EventPort {
    private final String serverId;
    private final StatefulRedisConnection<String, String> connection;
    private final RedisClient client;
    private final Gson mapper;

    @Inject
    public RedisEventPort(@AppModule.ServerId String serverId, @LettuceModule.DefaultConnection StatefulRedisConnection connection, RedisClient client, Gson mapper) {
        this.serverId = serverId;
        this.connection = connection;
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public void start(String channelId) {
        connection.sync().publish("inits", mapper.toJson(new InitEvent(serverId, channelId)));
    }

    @Override
    public void disconnect(String channelId) {
        connection.sync().publish("disconnections", mapper.toJson(new DisconnectEvent(serverId, channelId)));
    }

    @Override
    public void sendMessage(String channelId, String message) {
        MessageEvent player = new MessageEvent(serverId, channelId, message);
        connection.sync().publish("messages", mapper.toJson(player));
    }
}
