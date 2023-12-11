package me.ikurenkov.game.logic.adapter.out;

import com.google.gson.Gson;
import com.google.inject.Inject;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import me.ikurenkov.game.logic.application.port.out.DisconnectPort;
import me.ikurenkov.game.logic.application.port.out.MessagePort;
import me.ikurenkov.game.logic.configuration.LettuceModule;
import me.ikurenkov.game.logic.domain.PlayerId;


public class RedisQueuePort implements MessagePort, DisconnectPort {

    private final Gson mapper;
    private final StatefulRedisConnection<String, String> connection;

    @Inject
    public RedisQueuePort(Gson mapper, @LettuceModule.DefaultConnection StatefulRedisConnection connection) {
        this.mapper = mapper;
        this.connection = connection;
    }

    @Override
    public void disconnect(String serverId, String channelId, String message) {
        MessageCommand player = new MessageCommand(channelId, message);
        connection.sync().publish(STR."\{serverId}:disconnect", mapper.toJson(player));
    }

    @Override
    public void sendMessage(String serverId, String channelId, String message) {
        MessageCommand player = new MessageCommand(channelId, message);
        connection.sync().publish(STR."\{serverId}:message", mapper.toJson(player));
    }

    @Override
    public void sendMessage(PlayerId id, String message) {
        sendMessage(id.getServerId(), id.getChannelId(), message);
    }

    @Data
    @ToString
    @AllArgsConstructor
    public static class DisconnectCommand {
        private String serverId;
        private String channelId;
    }

    @Data
    @AllArgsConstructor
    public static class MessageCommand {
        private String channelId;
        private String message;
    }
}
