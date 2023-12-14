package me.ikurenkov.game.application;

public interface EventPort {

    void disconnect(String channelId);

    void sendMessage(String channelId, String message);
}
