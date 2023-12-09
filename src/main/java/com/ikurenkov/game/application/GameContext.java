package com.ikurenkov.game.application;

import com.ikurenkov.game.domain.Game;
import com.ikurenkov.game.domain.Player;

import java.util.Optional;

public interface GameContext {

    Optional<Player> getActor();

    Optional<Game> getGame();

    void setActor(Player actor);

    void setGame(Game game);
}
