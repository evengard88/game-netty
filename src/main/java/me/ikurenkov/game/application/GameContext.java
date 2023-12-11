package me.ikurenkov.game.application;

import me.ikurenkov.game.domain.Game;
import me.ikurenkov.game.domain.Player;

public interface GameContext {

    Player getActor();

    Game getGame();

    void setActor(Player actor);

    void setGame(Game game);
}
