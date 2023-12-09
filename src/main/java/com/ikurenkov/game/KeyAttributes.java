package com.ikurenkov.game;

import com.ikurenkov.game.domain.Player;
import com.ikurenkov.game.domain.Game;
import io.netty.util.AttributeKey;

public class KeyAttributes {
    public static final AttributeKey<Player> PLAYER_ATTRIBUTE_KEY = AttributeKey.valueOf("player");
    public static final AttributeKey<Game> GAME_ATTRIBUTE_KEY = AttributeKey.valueOf("game");
}
