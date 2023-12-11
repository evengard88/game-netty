package com.ikurenkov.game.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.ikurenkov.game.adapter.in.MessageHandler;
import com.ikurenkov.game.adapter.out.RedisEventPort;
import com.ikurenkov.game.application.EventPort;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class LettuceModule extends AbstractModule {
    private final String host;
    private final int port;

    public LettuceModule(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void configure() {
        String uri = "redis://" + host + ":" + port;
        bind(RedisClient.class).toProvider(RedisClientProvider.class).in(Singleton.class);
        bind(StatefulRedisConnection.class)
                .annotatedWith(DefaultConnection.class)
                .toProvider(new StatefulRedisConnectionProvider(RedisURI.create(uri)))
                .in(Singleton.class);
        bind(RedisURI.class).toProvider(() -> (RedisURI.create(uri))).asEagerSingleton();
        bind(StatefulRedisPubSubConnection.class)
                .annotatedWith(MessageListener.class)
                .toProvider(RedisPubSubMessageProvider.class)
                .asEagerSingleton();
        bind(StatefulRedisPubSubConnection.class)
                .annotatedWith(DisconnectListener.class)
                .toProvider(RedisPubSubDisconnectProvider.class)
                .asEagerSingleton();
        bind(EventPort.class).to(RedisEventPort.class).in(Singleton.class);
    }

    private static class RedisClientProvider implements Provider<RedisClient> {

        @Override
        public RedisClient get() {
            final RedisClient redisClient = RedisClient.create();
            Runtime.getRuntime().addShutdownHook(new Thread(redisClient::shutdown));
            return redisClient;
        }
    }

    private static class StatefulRedisConnectionProvider implements Provider<StatefulRedisConnection<?, ?>> {

        @Inject
        private RedisClient redisClient;

        private final RedisURI redisURI;

        public StatefulRedisConnectionProvider(RedisURI redisURI) {
            this.redisURI = redisURI;
        }

        @Override
        public StatefulRedisConnection<String, String> get() {
            return redisClient.connect(StringCodec.UTF8, redisURI);
        }
    }

    private static class RedisPubSubMessageProvider implements Provider<StatefulRedisPubSubConnection<String, String>> {

        @Inject
        private RedisClient redisClient;
        private final String serverId;
        @Inject
        private MessageHandler messageHandler;

        private final RedisURI redisURI;

        @Inject
        public RedisPubSubMessageProvider(RedisURI redisURI, @AppModule.ServerId String serverId) {
            this.redisURI = redisURI;
            this.serverId = serverId;
        }

        @Override
        public StatefulRedisPubSubConnection<String, String> get() {
            StatefulRedisPubSubConnection<String, String> connection = redisClient.connectPubSub(StringCodec.UTF8, redisURI);
            connection.addListener(messageHandler);
            connection.sync().subscribe(serverId + ":message");
            return connection;
        }
    }

    private static class RedisPubSubDisconnectProvider implements Provider<StatefulRedisPubSubConnection<String, String>> {

        @Inject
        private RedisClient redisClient;
        @Inject

        private String serverId;
        @Inject
        private MessageHandler disconnectHandler;

        private final RedisURI redisURI;

        @Inject
        public RedisPubSubDisconnectProvider(RedisURI redisURI, @AppModule.ServerId String serverId) {
            this.redisURI = redisURI;
            this.serverId = serverId;
        }

        @Override
        public StatefulRedisPubSubConnection<String, String> get() {
            StatefulRedisPubSubConnection<String, String> connection = redisClient.connectPubSub(StringCodec.UTF8, redisURI);
            connection.addListener(disconnectHandler);
            connection.sync().subscribe(serverId + ":disconnect");
            return connection;
        }
    }


    @Qualifier
    @Retention(RUNTIME)
    public @interface DefaultConnection {
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface MessageListener {
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface DisconnectListener {
    }
}


