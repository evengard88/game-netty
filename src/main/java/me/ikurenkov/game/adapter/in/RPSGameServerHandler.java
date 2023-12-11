package me.ikurenkov.game.adapter.in;

import com.google.inject.Inject;
import me.ikurenkov.game.adapter.mapper.ChannelHandlerContextAdapter;
import me.ikurenkov.game.adapter.mapper.ChannelHandlerMessageAdapter;
import me.ikurenkov.game.application.GameContext;
import me.ikurenkov.game.application.GameService;
import me.ikurenkov.game.application.PlayerMessagePort;
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
        gameService.start(mapToGameContext(ctx), mapToActorPort(ctx));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) {
        log.info("Handler, id = " + ctx.channel().id());
        gameService.handleMessage(mapToGameContext(ctx), message);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("Unregistered handler, id = " + ctx.channel().id());
        gameService.disconnect(mapToGameContext(ctx));
        super.channelUnregistered(ctx);
    }

    private static GameContext mapToGameContext(ChannelHandlerContext ctx) {
        return new ChannelHandlerContextAdapter(ctx);
    }

    private static PlayerMessagePort mapToActorPort(ChannelHandlerContext ctx) {
        return new ChannelHandlerMessageAdapter(ctx);
    }


}
