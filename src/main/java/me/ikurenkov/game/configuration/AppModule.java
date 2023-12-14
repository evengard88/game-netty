package me.ikurenkov.game.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import jakarta.inject.Qualifier;
import me.ikurenkov.game.application.ChannelMap;

import java.lang.annotation.Retention;
import java.util.UUID;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class AppModule extends AbstractModule {

    private final String serverId;

    public AppModule() {
        this(UUID.randomUUID().toString());
    }

    public AppModule(String serverId) {
        this.serverId = serverId;
    }

    @Provides
    @ServerId
    public String serverId() {
        return serverId;
    }

    @Singleton
    @Qualifier
    @Retention(RUNTIME)
    public @interface ServerId {
    }

    @Provides
    @Singleton
    public ChannelMap channelMap() {
        return new ChannelMap();
    }
}