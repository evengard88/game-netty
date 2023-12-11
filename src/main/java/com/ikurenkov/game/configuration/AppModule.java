package com.ikurenkov.game.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.util.UUID;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class AppModule extends AbstractModule {
    private final String serverId = UUID.randomUUID().toString();

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
}