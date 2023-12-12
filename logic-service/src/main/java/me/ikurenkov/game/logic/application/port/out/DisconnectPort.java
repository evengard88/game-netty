package me.ikurenkov.game.logic.application.port.out;

import me.ikurenkov.game.logic.domain.PlayerId;

public interface DisconnectPort {
    void disconnect(String serverId, String channelId, String message);
    void disconnect(PlayerId id, String message);
}
