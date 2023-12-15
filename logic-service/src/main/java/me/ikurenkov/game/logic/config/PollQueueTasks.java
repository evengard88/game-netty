package me.ikurenkov.game.logic.config;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.list.KeyValue;
import io.quarkus.redis.datasource.list.Position;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import me.ikurenkov.game.logic.application.port.in.LobbyStorePort;
import me.ikurenkov.game.logic.domain.Player;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * Smells bad.
 * It has to be listeners, and brpop operation seems to have bugs
 * <p>
 * I am sorry :..(
 */
@ApplicationScoped
public class PollQueueTasks {
    @Inject
    RedisDataSource ds;
    @Inject
    EventBus bus;
    @Inject
    LobbyStorePort lobbyStorePort;

    void onStart(@Observes StartupEvent ev) {
        Infrastructure.getDefaultWorkerPool().scheduleAtFixedRate(
                this::processMessage, 0, 20, TimeUnit.MILLISECONDS);
        Infrastructure.getDefaultWorkerPool().scheduleAtFixedRate(
                this::processDisconnectEvent, 0, 20, TimeUnit.MILLISECONDS);
        Infrastructure.getDefaultWorkerPool().scheduleAtFixedRate(
                this::processLobby, 0, 20, TimeUnit.MILLISECONDS);
    }

    public void processMessage() {
        Optional.ofNullable(
                        ds.list(InputMessageEvent.class).rpop("messages"))
                .ifPresent(message -> bus.send("inputMessageEvent", message));
    }

    public void processDisconnectEvent() {

        Optional.ofNullable(ds.list(DisconnectionEvent.class).rpop("disconnections"))
                .ifPresent(disconnectionEvent -> bus.send("disconnectionEvent", disconnectionEvent));
    }

    public void processLobby() {
        List<KeyValue<String, Player>> players = ds.list(Player.class).lmpop(Position.RIGHT, 2, "lobby");
        if (players.size() < 2) {
            players.forEach(p -> lobbyStorePort.store(p.value));
        } else {
            bus.send("playersFound", new PlayersFound(List.of(players.get(0).value, players.get(1).value)));
        }
    }

    @Data
    @ToString
    @AllArgsConstructor
    public static class InputMessageEvent {
        private String serverId;
        private String channelId;
        private String message;
    }

    @Data
    @ToString
    @AllArgsConstructor
    public static class DisconnectionEvent {
        private String serverId;
        private String channelId;
    }

    @Data
    @ToString
    @AllArgsConstructor
    public static class PlayersFound {
        private List<Player> players;
    }
}
