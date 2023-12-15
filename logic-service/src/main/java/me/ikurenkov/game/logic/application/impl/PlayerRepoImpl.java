package me.ikurenkov.game.logic.application.impl;

import io.quarkus.redis.datasource.RedisDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.ikurenkov.game.logic.application.port.out.PlayerStorePort;
import me.ikurenkov.game.logic.application.port.out.PlayerDeletePort;
import me.ikurenkov.game.logic.application.port.out.PlayerGetPort;
import me.ikurenkov.game.logic.domain.Player;
import me.ikurenkov.game.logic.domain.PlayerId;

import java.util.Optional;

@ApplicationScoped
public class PlayerRepoImpl implements PlayerStorePort, PlayerGetPort, PlayerDeletePort {

    @Inject
    RedisDataSource ds;

    @Override
    public Player getOrCreate(PlayerId playerId) {
        return Optional.of(ds.hash(Player.class))
                .map(e -> e.hget("player", playerId.toString()))
                .orElse(new Player().setPlayerId(playerId));
    }

    @Override
    public void store(Player player) {
        ds.hash(Player.class).hset("player", player.getPlayerId().toString(), player);
    }

    @Override
    public void delete(Player player) {
        this.delete(player.getPlayerId());
    }

    @Override
    public void delete(PlayerId playerId) {
        ds.hash(Player.class).hdel("player", playerId.toString());
    }
}

