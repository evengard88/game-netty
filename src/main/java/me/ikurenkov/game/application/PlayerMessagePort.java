package me.ikurenkov.game.application;

public interface PlayerMessagePort {
    void say(String message);
    void disconnect(String message);
}
