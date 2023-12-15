package me.ikurenkov.game.logic.application.port.in;


public interface PlayerDisconnectsUseCase {
    void playerDisconnect(String serverId, String channelId);
}
