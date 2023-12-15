package me.ikurenkov.game.logic.application.port.out;

import me.ikurenkov.game.logic.domain.PlayerId;

public interface MessagePort {
    void sendMessage(String serverId, String channelId, String message);

    void sendMessage(PlayerId id, String message);
}
