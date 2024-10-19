package me.ikurenkov.game.adapter.in;

import com.google.inject.Inject;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.java.Log;
import me.ikurenkov.game.application.ChannelMap;
import me.ikurenkov.game.application.EventPort;

@Log
@ChannelHandler.Sharable
public class RPSGameServerHandler extends SimpleChannelInboundHandler<String> {
    private final ChannelMap channelMap;
    private final EventPort eventPort;

    @Inject
    public RPSGameServerHandler(EventPort eventPort, ChannelMap channelMap) {
        this.eventPort = eventPort;
        this.channelMap = channelMap;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        channelMap.put(ctx.channel().id().asLongText(), ctx.channel());
        ctx.writeAndFlush("Hello! Enter your name, at least one symbol\n\r");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) {
        log.info("Handler, id = " + ctx.channel().id().asLongText());
        eventPort.sendMessage(ctx.channel().id().asLongText(), message);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("Unregistered handler, id = " + ctx.channel().id().asLongText());
        eventPort.disconnect(ctx.channel().id().asLongText());
        channelMap.remove(ctx.channel().id().asLongText());
        super.channelUnregistered(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info(evt.getClass().toString());

        if (evt instanceof IdleStateEvent) {
            Boolean firstTime = ctx.channel().<Boolean>attr(AttributeKey.valueOf("timeout")).setIfAbsent(true);
            if (firstTime == null) {
                ctx.channel().writeAndFlush("\n\rYou will be disconnected if you are idle\n\r");
            } else {
                ctx.channel().writeAndFlush("\n\rYou are disconnected for being idle. Good buy!\n\r")
                        .addListener(ChannelFutureListener.CLOSE);
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
