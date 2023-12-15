package me.ikurenkov.game.logic.application.port.in;

public interface MessageReceivedUseCase {
    void handleMessage(String serverId, String channelId, String message);
}
