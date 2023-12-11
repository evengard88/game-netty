package me.ikurenkov.game.application.impl;

import com.google.inject.Inject;
import me.ikurenkov.game.configuration.NettyModule;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.java.Log;

@Log
public class NettyServer {
    private final int port;
    private final ChannelInitializer<NioSocketChannel> gameHandlerInitializer;

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    @Inject
    public NettyServer(@NettyModule.Port int port,
                       ChannelInitializer<NioSocketChannel> gameHandler,
                       @NettyModule.BossGroupLoop EventLoopGroup bossgroup,
                       @NettyModule.WorkerGroupLoop EventLoopGroup workergroup) {
        this.port = port;
        this.bossGroup = bossgroup;
        this.workerGroup = workergroup;
        this.gameHandlerInitializer = gameHandler;
    }

    public void run() {
        log.info("Server is running on port " + port);
        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(gameHandlerInitializer);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.severe(e.toString());
        } finally {
            log.info("Server shutdown gracefully");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
	