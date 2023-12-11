package me.ikurenkov.game.application;

public interface GameHandler {
    void handle(GameContext context, String message);
    boolean supports(GameContext context);
}

