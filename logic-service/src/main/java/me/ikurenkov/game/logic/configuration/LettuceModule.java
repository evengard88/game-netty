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
import me.ikurenkov.game.logic.adapter.in.AbstractHandler;
import me.ikurenkov.game.logic.adapter.in.DisconnectHandler;
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

        bind(AbstractHandler.class)
                .annotatedWith(MessageListener.class)
                .to(MessageHandler.class)
                .in(Singleton.class);

        bind(AbstractHandler.class)
                .annotatedWith(DisconnectListener.class)
                .to(DisconnectHandler.class)
                .in(Singleton.class);

        bind(StatefulRedisPubSubConnection.class)
                .annotatedWith(MessageListener.class)
                .toProvider(RedisListerMessageProvider.class)
                .asEagerSingleton();
        bind(StatefulRedisPubSubConnection.class)
                .annotatedWith(DisconnectListener.class)
                .toProvider(RedisListerDisconnectProvider.class)
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

    private static class RedisListerMessageProvider extends RedisListerAbstractProvider {
        @Inject
        public RedisListerMessageProvider(RedisClient client, @MessageListener AbstractHandler handler, RedisURI uri) {
            super(client, handler, uri, "messages");
        }
    }

    private static class RedisListerDisconnectProvider extends RedisListerAbstractProvider {
        @Inject
        public RedisListerDisconnectProvider(RedisClient client, @DisconnectListener AbstractHandler handler, RedisURI uri) {
            super(client, handler, uri, "disconnections");
        }
    }

    private static class RedisListerAbstractProvider implements Provider<StatefulRedisPubSubConnection<String, String>> {

        private final RedisClient redisClient;
        private final AbstractHandler handler;
        private final RedisURI redisURI;
        private final String queue;

        private RedisListerAbstractProvider(RedisClient redisClient,
                                            AbstractHandler handler,
                                            RedisURI redisURI,
                                            String queue) {
            this.redisClient = redisClient;
            this.handler = handler;
            this.redisURI = redisURI;
            this.queue = queue;
        }


        @Override
        public StatefulRedisPubSubConnection<String, String> get() {
            StatefulRedisPubSubConnection<String, String> connection = redisClient.connect(StringCodec.UTF8, redisURI);
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
    public @interface MessageListener {
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface DisconnectListener {
    }
}


