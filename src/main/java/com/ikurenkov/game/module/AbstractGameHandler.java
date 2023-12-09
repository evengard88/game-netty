package com.ikurenkov.game.module;

import com.ikurenkov.game.domain.Player;

public abstract class AbstractGameHandler {
    public abstract String handle(GameContext context, String message);

}

