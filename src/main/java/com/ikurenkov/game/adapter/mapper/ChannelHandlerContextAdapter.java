package com.ikurenkov.game.adapter.mapper;

import com.ikurenkov.game.application.GameContext;
import com.ikurenkov.game.domain.Game;
import com.ikurenkov.game.domain.Player;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class ChannelHandlerContextAdapter implements GameContext {
    private final Channel serverChanel;

    public ChannelHandlerContextAdapter(ChannelHandlerContext ctx) {
        this.serverChanel = ctx.channel();
    }

    @Override
    public Player getActor() {
        return serverChanel.attr(KeyAttributes.PLAYER_ATTRIBUTE_KEY).get();
    }

    @Override
    public Game getGame() {
        return serverChanel.attr(KeyAttributes.GAME_ATTRIBUTE_KEY).get();
    }

    @Override
    public void setActor(Player actor) {
        serverChanel.attr(KeyAttributes.PLAYER_ATTRIBUTE_KEY).set(actor);
    }

    @Override
    public void setGame(Game game) {
        serverChanel.attr(KeyAttributes.GAME_ATTRIBUTE_KEY).set(game);
    }

    private static class KeyAttributes {
        static final AttributeKey<Player> PLAYER_ATTRIBUTE_KEY = AttributeKey.valueOf("player");
        static final AttributeKey<Game> GAME_ATTRIBUTE_KEY = AttributeKey.valueOf("game");
    }
}