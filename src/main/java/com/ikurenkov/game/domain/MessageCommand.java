package com.ikurenkov.game.domain;

import lombok.Data;

@Data
public class MessageCommand {
    private String channelId;
    private String message;
}
