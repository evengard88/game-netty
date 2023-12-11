package me.ikurenkov.game.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class MessageEvent {
    String serverId;
    String channelId;
    String message;
}
