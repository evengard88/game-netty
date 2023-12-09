package com.ikurenkov.game.application;

import com.ikurenkov.game.domain.Game;
import com.ikurenkov.game.domain.Player;

public interface GameContext {

    Player getActor();

    Game getGame();

    void setActor(Player actor);

    void setGame(Game game);
}
