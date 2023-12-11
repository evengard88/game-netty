package me.ikurenkov.game.logic.application.port.in;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

public interface PlayerDisconnectsUseCase {
    void playerDisconnect(String serverId, String channelId);

    @Data
    @ToString
    @AllArgsConstructor
    class Disconnection {
        private String serverId;
        private String channelId;
    }
}
