package com.ikurenkov.game.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class InitEvent {
    String serverId;
    String channelId;
}
