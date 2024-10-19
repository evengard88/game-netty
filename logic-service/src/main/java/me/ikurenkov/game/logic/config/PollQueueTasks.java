package me.ikurenkov.game.logic.config;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.list.KeyValue;
import io.quarkus.redis.datasource.list.Position;
import io.quarkus.redis.datasource.list.ReactiveTransactionalListCommands;
import io.quarkus.redis.datasource.transactions.OptimisticLockingTransactionResult;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.ShutdownEvent;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.Cancellable;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.Future;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.redis.client.RedisAPI;
import io.vertx.mutiny.redis.client.Response;
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

import static io.vertx.redis.client.Command.EVAL;


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
    RedisAPI api;
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

        String luaScript =
                "local list_key = KEYS[1]\n" +
                        "local pair_list_key = KEYS[2]\n" +
                        "local count = redis.call('LLEN', list_key)\n" +
                        "if count >= 2 then\n" +
                        "     redis.call('LMOVE', 'lobby', 'lobby_pairs', 'LEFT', 'RIGHT')" +
                        "    redis.call('LMOVE', 'lobby', 'lobby_pairs', 'LEFT', 'RIGHT')" +
                        "end";
        Uni<Void> luaExecution = ds.execute(EVAL, luaScript, "2", "lobby", "lobby_pairs")
                .onItem().ignore().andContinueWithNull();  // Ensuring it emits Void and completes.

        luaExecution
                .chain(() -> ds.list(Player.class)
                        .blmpop(Duration.ofMillis(1000), Position.RIGHT, 2, "lobby_pairs"))  // Execute blmpop after Lua execution
                .repeat()
                .indefinitely()
                .filter(players -> players != null && players.size() == 2)  // Ensure valid result from blmpop
                .subscribe()
                .with(
                        players -> {
                            bus.send("playersFound", new PlayersFound(List.of(players.get(0).value(), players.get(1).value())));
                        },
                        failure -> log.error("Error in loop iteration", failure)
                );
//        Future<@Nullable Response> eval =
//            api.eval(List.of(luaScript, "1", "lobby"))
//
//                    .repeat().withDelay(Duration.ofMillis(6000)).indefinitely().subscribe()
//
//                    .with(item-> System.out.println(item));



//        ds.execute(luaScript).subscribe().with(item->{
//            item.forEach(i->{
//                System.out.println(i);
//                i.forEach(i2-> System.out.println(i2));
//        });
//        ds.list(Player.class)
//                .blmpop(Duration.ofMillis(1000), Position.RIGHT, 2, "lobby")
//                .repeat().indefinitely()
//                .invoke(players -> {
//                    System.out.println("players: " + players);
//                    if (players.size() < 2) {
//                        CompletableFuture.runAsync(() -> players.forEach(p -> lobbyStorePort.store(p.value)));
//                    } else {
//                        bus.send("playersFound", new PlayersFound(List.of(players.get(0).value, players.get(1).value)));
//                    }
//                })
//                .subscribe().with(item -> {}, throwable -> {
//                    // Handle error
//                    throwable.printStackTrace();
//                });
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
