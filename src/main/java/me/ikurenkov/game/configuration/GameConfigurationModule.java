package me.ikurenkov.game.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import me.ikurenkov.game.GameServiceImpl;
import me.ikurenkov.game.application.GameService;

public class GameConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();
        bind(GameService.class).to(GameServiceImpl.class).in(Singleton.class);
    }
}