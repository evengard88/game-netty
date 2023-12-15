package me.ikurenkov.game.adapter.out;

import com.google.gson.Gson;
import com.google.inject.Inject;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import me.ikurenkov.game.application.EventPort;
import me.ikurenkov.game.configuration.AppModule;
import me.ikurenkov.game.configuration.LettuceModule;
import me.ikurenkov.game.domain.DisconnectEvent;
import me.ikurenkov.game.domain.MessageEvent;

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
    public void disconnect(String channelId) {
        connection.sync().lpush("disconnections", mapper.toJson(new DisconnectEvent(serverId, channelId)));
    }

    @Override
    public void sendMessage(String channelId, String message) {
        MessageEvent messageEvent = new MessageEvent(serverId, channelId, message);
        connection.sync().lpush("messages", mapper.toJson(messageEvent));
//        connection.sync().lpush("messages", mapper.toJson(messageEvent));
    }
}
