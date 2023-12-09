package com.ikurenkov.game.application;

import com.ikurenkov.game.application.GameContext;

public interface GameService {
    void start(GameContext context, PlayerMessagePort actorPort);
    void handleMessage(GameContext context, String message);
    void disconnect(GameContext context);
}
