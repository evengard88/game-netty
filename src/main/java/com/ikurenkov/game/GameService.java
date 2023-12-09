package com.ikurenkov.game;

import com.ikurenkov.game.module.GameContext;

public interface GameService {
    String start(GameContext context);
    String handleMessage(GameContext context, String message);

    String disconnect(GameContext context);

}
