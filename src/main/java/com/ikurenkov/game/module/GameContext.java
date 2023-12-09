package com.ikurenkov.game.module;

import com.ikurenkov.game.domain.Game;
import com.ikurenkov.game.domain.Player;

public interface GameContext {

    Player getActor();
    Game getGame();
}
