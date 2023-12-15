package me.ikurenkov.game.logic.application.port.out;

import me.ikurenkov.game.logic.domain.Player;
import me.ikurenkov.game.logic.domain.PlayerId;

public interface PlayerDeletePort {
    void delete(Player player);

    void delete(PlayerId playerId);
}
