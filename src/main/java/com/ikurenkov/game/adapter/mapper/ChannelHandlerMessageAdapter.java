package com.ikurenkov.game.adapter.mapper;

import com.ikurenkov.game.application.PlayerMessagePort;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

public class ChannelHandlerMessageAdapter implements PlayerMessagePort {
    private final Channel serverChanel;

    public ChannelHandlerMessageAdapter(ChannelHandlerContext ctx) {
        this.serverChanel = ctx.channel();
    }

    @Override
    public void say(String message) {
        serverChanel.writeAndFlush(message);
    }

    @Override
    public void disconnect(String message) {
        serverChanel.writeAndFlush(message).addListener(ChannelFutureListener.CLOSE);
    }
}