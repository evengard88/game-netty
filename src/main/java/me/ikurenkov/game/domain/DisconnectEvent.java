package me.ikurenkov.game.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class DisconnectEvent {
    String serverId;
    String channelId;
}
