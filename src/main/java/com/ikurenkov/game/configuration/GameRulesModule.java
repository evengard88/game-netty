package com.ikurenkov.game.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.ikurenkov.game.application.GameHandler;
import com.ikurenkov.game.application.handler.GameStartHandler;
import com.ikurenkov.game.application.handler.PlayerEnterMoveHandler;
import com.ikurenkov.game.application.handler.PlayerEnterNameHandler;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class GameRulesModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<GameHandler> playerHandlers =
                Multibinder.newSetBinder(binder(), GameHandler.class);
        playerHandlers.addBinding().to(PlayerEnterNameHandler.class).in(Singleton.class);
        playerHandlers.addBinding().to(PlayerEnterMoveHandler.class).in(Singleton.class);

        bind(GameHandler.class).to(GameStartHandler.class).in(Singleton.class);
    }


    @Singleton
    @Qualifier
    @Retention(RUNTIME)
    public @interface GameStart {
    }
}
