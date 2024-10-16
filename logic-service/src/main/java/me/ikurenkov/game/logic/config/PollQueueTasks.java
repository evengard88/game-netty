package me.ikurenkov.game.logic.config;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.list.KeyValue;
import io.quarkus.redis.datasource.list.Position;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.ShutdownEvent;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.Cancellable;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import me.ikurenkov.game.logic.application.port.in.LobbyStorePort;
import me.ikurenkov.game.logic.domain.Player;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * Smells bad.
 * It has to be listeners, and brpop operation seems to have bugs
 * <p>
 */
@Slf4j
@ApplicationScoped
public class PollQueueTasks {
    @Inject
    ReactiveRedisDataSource ds;
    @Inject
    EventBus bus;
    @Inject
    LobbyStorePort lobbyStorePort;

    private Cancellable messageCancelable;
    private Cancellable disconnectCancelable;
    private Cancellable lobbyCancelable;

    void onStart(@Observes StartupEvent ev) {
        processMessage();
        processDisconnectEvent();
        processLobby();
    }

    public void onStop(@Observes ShutdownEvent ev) {
        // Остановить прослушивание при завершении приложения
        if (messageCancelable != null) {
            messageCancelable.cancel();
        }
        if (disconnectCancelable != null) {
            disconnectCancelable.cancel();
        }
        if (lobbyCancelable != null) {
            lobbyCancelable.cancel();
        }
    }

    public void processMessage() {
        messageCancelable = ds.list(InputMessageEvent.class)
                .brpop(Duration.ofMillis(1000), "messages")
                .repeat().indefinitely()
                .filter(p -> p != null && p.value != null)
                .onItem().invoke(item -> {
                    if (item != null) {
                        bus.send("inputMessageEvent", item.value);
                    }
                })
                .subscribe()
                .with(
                        ignored -> {
                        },
                        failure -> log.error("Error in loop iteration", failure));
    }

    public void processDisconnectEvent() {
        disconnectCancelable =
                ds.list(DisconnectionEvent.class)
                        .brpop(Duration.ofMillis(1000), "disconnections")
                        .repeat().indefinitely()
                        .filter(p -> p != null && p.value != null)
                        .onItem().invoke(item -> {
                            if (item != null) {
                                bus.send("disconnectionEvent", item.value);
                            }
                        })
                        .subscribe()
                        .with(
                                ignored -> {
                                },
                                failure -> log.error("Error in loop iteration", failure));
    }

    public void processLobby() {
        lobbyCancelable = ds.list(Player.class)
                .blmpop(Duration.ofMillis(1000), Position.RIGHT, 2, "lobby")
                .repeat().indefinitely()
                .subscribe()
                .with(players -> {
                    CompletableFuture.runAsync(() -> {
                        if (players.size() < 2) {
                            players.forEach(p -> lobbyStorePort.store(p.value));
                        } else {
                            bus.send("playersFound", new PlayersFound(List.of(players.get(0).value, players.get(1).value)));
                        }
                    });
                });
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
