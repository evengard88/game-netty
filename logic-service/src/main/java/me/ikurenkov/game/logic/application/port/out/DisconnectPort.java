package me.ikurenkov.game.logic.application.port.out;

public interface DisconnectPort {
    void disconnect(String serverId, String channelId, String message);
}
