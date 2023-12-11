package com.ikurenkov.game.adapter.in;

import com.google.inject.Inject;
import com.ikurenkov.game.application.EventPort;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.java.Log;

@Log
@ChannelHandler.Sharable
public class RPSGameServerHandler extends SimpleChannelInboundHandler<String> {
    private final ChannelGroup channelGroup;
    private final EventPort eventPort;

    @Inject
    public RPSGameServerHandler(EventPort eventPort, ChannelGroup channelGroup) {
        this.eventPort = eventPort;
        this.channelGroup = channelGroup;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        channelGroup.add(ctx.channel());
        eventPort.start(ctx.channel().id().asLongText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) {
        log.info("Handler, id = " + ctx.channel().id());
        eventPort.sendMessage(ctx.channel().id().asLongText(), message);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("Unregistered handler, id = " + ctx.channel().id());
        eventPort.disconnect(ctx.channel().id().asLongText());
        super.channelUnregistered(ctx);
    }

}
