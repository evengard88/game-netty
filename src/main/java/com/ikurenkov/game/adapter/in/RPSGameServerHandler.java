package com.ikurenkov.game.adapter.in;

import com.google.inject.Inject;
import com.ikurenkov.game.GameService;
import com.ikurenkov.game.KeyAttributes;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.java.Log;

@Log
@ChannelHandler.Sharable
public class RPSGameServerHandler extends SimpleChannelInboundHandler<String> {
    private final GameService gameService;

    @Inject
    public RPSGameServerHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        ctx.writeAndFlush("Hello! Enter your name, at least one symbol\n\r");
        gameService.start(null);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws InterruptedException {
        log.info("Handler, id = " + ctx.channel().id().asLongText()
                + " player " + ctx.channel().attr(KeyAttributes.PLAYER_ATTRIBUTE_KEY));
        gameService.handleMessage(null, message);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("Unregistered handler, id = " + ctx.channel().id());
        gameService.disconnect(null);
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Inactive handler, id = " + ctx.channel().id());
        super.channelInactive(ctx);
    }
}
