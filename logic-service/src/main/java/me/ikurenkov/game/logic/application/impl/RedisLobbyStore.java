package me.ikurenkov.game.logic.application.impl;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import me.ikurenkov.game.logic.application.port.in.LobbyStorePort;
import me.ikurenkov.game.logic.domain.Player;

@ApplicationScoped
@Startup
public class RedisLobbyStore implements LobbyStorePort {
    private final RedisDataSource ds;

    public RedisLobbyStore(RedisDataSource ds) {
        this.ds = ds;
    }

    public void store(Player p) {
        ds.list(Player.class).lpush("lobby", p);
    }

    @Override
    public void remove(Player p) {
        ds.list(Player.class).lrem("lobby", 1, p);
    }
}
