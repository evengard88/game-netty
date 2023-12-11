package me.ikurenkov.game.application;

import me.ikurenkov.game.application.GameContext;

public interface GameService {
    void start(GameContext context, PlayerMessagePort actorPort);
    void handleMessage(GameContext context, String message);
    void disconnect(GameContext context);
}
