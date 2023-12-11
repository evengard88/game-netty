package me.ikurenkov.game.logic.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PlayerId {
    private String serverId;
    private String channelId;
}
