package me.ikurenkov.game.logic.application.port.in;

import me.ikurenkov.game.logic.domain.Player;
import me.ikurenkov.game.logic.domain.PlayerId;

public interface LobbyPlayerStorePort {
    void store(PlayerId p);
    void remove(PlayerId playerId);
}
