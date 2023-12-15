package me.ikurenkov.game.logic.adapter.out;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import me.ikurenkov.game.logic.application.port.out.DisconnectPort;
import me.ikurenkov.game.logic.application.port.out.MessagePort;
import me.ikurenkov.game.logic.domain.PlayerId;

@ApplicationScoped
@Startup
public class RedisQueue implements MessagePort, DisconnectPort {
    private final RedisDataSource ds;

    public RedisQueue(RedisDataSource ds) {
        this.ds = ds;
    }

    @Override
    public void disconnect(PlayerId id, String message) {
        this.disconnect(id.getServerId(), id.getChannelId(), message);
    }

    @Override
    public void disconnect(String serverId, String channelId, String message) {
        ds.pubsub(DisconnectCommand.class)
                .publish(STR."\{serverId}:disconnect",
                        new DisconnectCommand(channelId, message));
    }

    @Override
    public void sendMessage(String serverId, String channelId, String message) {
        ds.pubsub(MessageCommand.class)
                .publish(STR."\{serverId}:message",
                        new MessageCommand(channelId, message));
    }

    @Override
    public void sendMessage(PlayerId id, String message) {
        sendMessage(id.getServerId(), id.getChannelId(), message);
    }

    @Data
    @ToString
    @AllArgsConstructor
    public static class DisconnectCommand {
        private String channelId;
        private String message;
    }

    @Data
    @AllArgsConstructor
    public static class MessageCommand {
        private String channelId;
        private String message;
    }
}
