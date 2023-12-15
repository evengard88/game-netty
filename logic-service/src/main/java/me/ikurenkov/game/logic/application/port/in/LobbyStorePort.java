package me.ikurenkov.game.logic.application.port.in;

import me.ikurenkov.game.logic.domain.Player;

public interface LobbyStorePort {

    void store(Player p);

    void remove(Player player);
}
