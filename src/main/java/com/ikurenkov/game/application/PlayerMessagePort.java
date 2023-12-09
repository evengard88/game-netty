package com.ikurenkov.game.application;

public interface PlayerMessagePort {
    void say(String message);
    void disconnect(String message);
}
