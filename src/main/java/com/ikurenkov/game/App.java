package com.ikurenkov.game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ikurenkov.game.configuration.AppDataModule;
import com.ikurenkov.game.module.GameConfigurationModule;
import com.ikurenkov.game.configuration.NettyModule;
import lombok.extern.java.Log;

@Log
public class App {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new GameConfigurationModule(),
                new NettyModule(Integer.parseInt(args[0])),
                new AppDataModule()
                );
        final NettyServer server = injector.getInstance(NettyServer.class);
        server.run();
    }
}