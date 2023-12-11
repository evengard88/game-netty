package me.ikurenkov.game.logic.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import jakarta.inject.Qualifier;
import me.ikurenkov.game.logic.adapter.in.AbstractListener;
import me.ikurenkov.game.logic.adapter.in.DisconnectHandler;
import me.ikurenkov.game.logic.adapter.in.InitHandler;
import me.ikurenkov.game.logic.adapter.in.MessageHandler;
import me.ikurenkov.game.logic.adapter.out.RedisQueuePort;
import me.ikurenkov.game.logic.application.port.out.DisconnectPort;
import me.ikurenkov.game.logic.application.port.out.MessagePort;

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

        bind(MessagePort.class).to(RedisQueuePort.class).asEagerSingleton();
        bind(DisconnectPort.class).to(RedisQueuePort.class).asEagerSingleton();

        bind(AbstractListener.class)
                .annotatedWith(InitListener.class)
                .to(InitHandler.class)
                .in(Singleton.class);

        bind(AbstractListener.class)
                .annotatedWith(MessageListener.class)
                .to(MessageHandler.class)
                .in(Singleton.class);

        bind(AbstractListener.class)
                .annotatedWith(DisconnectListener.class)
                .to(DisconnectHandler.class)
                .in(Singleton.class);

        bind(StatefulRedisPubSubConnection.class)
                .annotatedWith(InitListener.class)
                .toProvider(RedisPubSubInitProvider.class)
                .asEagerSingleton();
        bind(StatefulRedisPubSubConnection.class)
                .annotatedWith(MessageListener.class)
                .toProvider(RedisPubSubMessageProvider.class)
                .asEagerSingleton();
        bind(StatefulRedisPubSubConnection.class)
                .annotatedWith(DisconnectListener.class)
                .toProvider(RedisPubSubDisconnectProvider.class)
                .asEagerSingleton();

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

    private static class RedisPubSubInitProvider extends RedisPubSubAbstractProvider {
        @Inject
        public RedisPubSubInitProvider(RedisClient client, @InitListener AbstractListener handler, RedisURI uri) {
            super(client, handler, uri, "inits");
        }
    }

    private static class RedisPubSubMessageProvider extends RedisPubSubAbstractProvider {
        @Inject
        public RedisPubSubMessageProvider(RedisClient client, @MessageListener AbstractListener handler, RedisURI uri) {
            super(client, handler, uri, "messages");
        }
    }

    private static class RedisPubSubDisconnectProvider extends RedisPubSubAbstractProvider {
        @Inject
        public RedisPubSubDisconnectProvider(RedisClient client, @DisconnectListener AbstractListener handler, RedisURI uri) {
            super(client, handler, uri, "disconnections");
        }
    }

    private static class RedisPubSubAbstractProvider implements Provider<StatefulRedisPubSubConnection<String, String>> {

        private final RedisClient redisClient;
        private final AbstractListener handler;
        private final RedisURI redisURI;
        private final String queue;

        private RedisPubSubAbstractProvider(RedisClient redisClient,
                                            AbstractListener handler,
                                            RedisURI redisURI,
                                            String queue) {
            this.redisClient = redisClient;
            this.handler = handler;
            this.redisURI = redisURI;
            this.queue = queue;
        }


        @Override
        public StatefulRedisPubSubConnection<String, String> get() {
            StatefulRedisPubSubConnection<String, String> connection = redisClient.connectPubSub(StringCodec.UTF8, redisURI);
            connection.addListener(handler);
            connection.sync().subscribe(queue);
            return connection;
        }
    }


    @Qualifier
    @Retention(RUNTIME)
    public @interface DefaultConnection {
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface InitListener {
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


