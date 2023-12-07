package com.ikurenkov.game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ikurenkov.game.module.AppDataModule;
import com.ikurenkov.game.module.NettyModule;
import lombok.extern.java.Log;

@Log
public class App {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new NettyModule(Integer.parseInt(args[0])), new AppDataModule());
        final NettyServer server = injector.getInstance(NettyServer.class);
        server.run();
    }
}