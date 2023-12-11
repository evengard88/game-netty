package me.ikurenkov.game.logic.application.impl;

import me.ikurenkov.game.logic.application.port.in.LobbyPlayerStorePort;
import me.ikurenkov.game.logic.application.port.out.LobbyPlayerGetPort;
import me.ikurenkov.game.logic.domain.Player;
import me.ikurenkov.game.logic.domain.PlayerId;

public class RedisLobbyPort implements LobbyPlayerGetPort, LobbyPlayerStorePort {

    @Override
    public void store(Player p) {
        throw new UnsupportedOperationException("for now store player");
    }

    @Override
    public void remove(PlayerId p) {
        throw new UnsupportedOperationException("for now remove player");
    }

    @Override
    public Player getAny() {
        return null;
    }
}
