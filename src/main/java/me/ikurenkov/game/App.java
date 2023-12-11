package me.ikurenkov.game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import me.ikurenkov.game.configuration.AppDataModule;
import me.ikurenkov.game.configuration.GameRulesModule;
import me.ikurenkov.game.configuration.NettyModule;
import me.ikurenkov.game.configuration.GameConfigurationModule;
import lombok.extern.java.Log;

@Log
public class App {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new GameConfigurationModule(),
                new NettyModule(Integer.parseInt(args[0])),
                new AppDataModule(),
                new GameRulesModule(),
                new GameConfigurationModule()
        );
        final NettyServer server = injector.getInstance(NettyServer.class);
        server.run();
    }
}