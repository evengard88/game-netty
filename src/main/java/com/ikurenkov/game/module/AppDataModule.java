package com.ikurenkov.game.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.ikurenkov.game.Player;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AppDataModule extends AbstractModule {

    @Provides
    @Singleton
    public BlockingQueue<Player> provideLobby() {
        return new LinkedBlockingQueue<>();
    }

}