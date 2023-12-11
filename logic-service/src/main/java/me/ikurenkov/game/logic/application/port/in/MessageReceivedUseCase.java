package me.ikurenkov.game.logic.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

public interface MessageReceivedUseCase {
    void handleMessage(String serverId, String channelId, String message);

    @Data
    @ToString
    @AllArgsConstructor
    class InputMessage {
        private String serverId;
        private String channelId;
        private String message;
    }
}
