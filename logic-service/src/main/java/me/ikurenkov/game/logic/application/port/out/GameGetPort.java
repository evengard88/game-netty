package me.ikurenkov.game.logic.application.port.out;

import me.ikurenkov.game.logic.domain.Game;
import me.ikurenkov.game.logic.domain.PlayerId;

public interface GameGetPort {
    Game get(PlayerId playerId);
    Game get(String gameId);
}
