package me.ikurenkov.game.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import me.ikurenkov.game.application.GameHandler;
import me.ikurenkov.game.application.handler.GameStartHandler;
import me.ikurenkov.game.application.handler.PlayerEnterMoveHandler;
import me.ikurenkov.game.application.handler.PlayerEnterNameHandler;
import me.ikurenkov.game.application.handler.WaitForOpponentHandler;

public class GameRulesModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<GameHandler> playerHandlers =
                Multibinder.newSetBinder(binder(), GameHandler.class);
        playerHandlers.addBinding().to(PlayerEnterNameHandler.class).in(Singleton.class);
        playerHandlers.addBinding().to(PlayerEnterMoveHandler.class).in(Singleton.class);
        playerHandlers.addBinding().to(WaitForOpponentHandler.class).in(Singleton.class);

        bind(GameHandler.class).to(GameStartHandler.class).in(Singleton.class);
    }
}
