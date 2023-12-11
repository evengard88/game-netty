package me.ikurenkov.game.logic.application.port.out;

import me.ikurenkov.game.logic.domain.Game;

public interface GameUpdatePort {
    Game gameUpdate(Game game);
}
