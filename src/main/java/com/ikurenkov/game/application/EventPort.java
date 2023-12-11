package com.ikurenkov.game.application;

public interface EventPort {

    void start(String channelId);

    void disconnect(String channelId);

    void sendMessage(String channelId, String message);
}
