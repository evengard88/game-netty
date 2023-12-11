package me.ikurenkov.game.logic.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

public interface PlayerInitializedUseCase {
    void playerInitialize(String serverId, String channelId);

    @Data
    @ToString
    @AllArgsConstructor
    class Initialization {
        private String serverId;
        private String channelId;
    }
}
