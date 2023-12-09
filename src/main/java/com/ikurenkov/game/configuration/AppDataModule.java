package com.ikurenkov.game.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.ikurenkov.game.application.GameContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AppDataModule extends AbstractModule {

    @Provides
    @Singleton
    public BlockingQueue<GameContext> provideLobby() {
        return new LinkedBlockingQueue<>();
    }

}