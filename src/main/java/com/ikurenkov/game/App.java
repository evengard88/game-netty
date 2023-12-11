package com.ikurenkov.game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ikurenkov.game.configuration.*;
import lombok.extern.java.Log;

@Log
public class App {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new NettyModule(Integer.parseInt(args[0])),
                new LettuceModule(args[1], Integer.parseInt(args[2])),
                new AppModule(),
                new JsonMapperModule()
        );
        final NettyServer server = injector.getInstance(NettyServer.class);
        server.run();
    }
}