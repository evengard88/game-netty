package me.ikurenkov.game.logic.application.port.out;

import me.ikurenkov.game.logic.domain.Player;

public interface PlayerStorePort {
    void store(Player player);
}
