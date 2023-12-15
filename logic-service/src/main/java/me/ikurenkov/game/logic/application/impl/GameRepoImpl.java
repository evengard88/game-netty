package me.ikurenkov.game.logic.application.impl;

import io.quarkus.redis.datasource.RedisDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import me.ikurenkov.game.logic.application.port.out.GameStorePort;
import me.ikurenkov.game.logic.application.port.out.GameDeletePort;
import me.ikurenkov.game.logic.application.port.out.GameGetPort;
import me.ikurenkov.game.logic.domain.Game;

@ApplicationScoped
public class GameRepoImpl implements GameGetPort, GameStorePort, GameDeletePort {

    private final RedisDataSource ds;

    public GameRepoImpl(RedisDataSource ds) {
        this.ds = ds;
    }

    @Override
    public Game getById(String gameId) {
        return ds.hash(Game.class).hget("games", gameId);
    }

    @Override
    public void store(Game game) {
        ds.hash(Game.class).hset("games", game.getGameId(), game);
    }

    @Override
    public void delete(Game game) {
        ds.hash(Game.class).hdel("games", game.getGameId());
    }
}

