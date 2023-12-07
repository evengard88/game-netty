package com.ikurenkov.game;

import io.netty.channel.Channel;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class Player {
    private final Channel chanel;
    private volatile String name;
    private volatile Move move;
}
