package me.ikurenkov.game.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import me.ikurenkov.game.NettyServer;
import me.ikurenkov.game.adapter.in.RPSGameServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class NettyModule extends AbstractModule {

    public final int port;

    public NettyModule(int port) {
        this.port = port;
    }

    @Override
    protected void configure() {
        bind(NettyServer.class);
    }

    @Provides
    @BossGroupLoop
    @Singleton
    public EventLoopGroup providesBossEventLoopGroup() {
        return new NioEventLoopGroup(1);
    }

    @Provides
    @WorkerGroupLoop
    @Singleton
    public EventLoopGroup providesWorkerEventLoopGroup() {
        return new NioEventLoopGroup();
    }

    @Provides
    @Singleton
    public ChannelInitializer<NioSocketChannel> channelInitializer(StringEncoder encoder,
                                                                   StringDecoder decoder,
                                                                   RPSGameServerHandler gameHandler) {
        return new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                pipeline.addLast(encoder);
                pipeline.addLast(decoder);
                pipeline.addLast(gameHandler);
            }
        };
    }

//    @Provides
//    @Singleton
//    public ChannelGroup channelGroup() {
//        return new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
//    }

    @Provides
    @Port
    @Singleton
    public int getPort() {
        return port;
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface Port {
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface BossGroupLoop {
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface WorkerGroupLoop {
    }

}