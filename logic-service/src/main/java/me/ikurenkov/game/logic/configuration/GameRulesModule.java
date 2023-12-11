package me.ikurenkov.game.logic.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import me.ikurenkov.game.logic.application.GameHandler;
import me.ikurenkov.game.logic.application.impl.handler.GameStartHandler;
import me.ikurenkov.game.logic.application.impl.handler.PlayerEnterMoveHandler;
import me.ikurenkov.game.logic.application.impl.handler.PlayerEnterNameHandler;
import me.ikurenkov.game.logic.application.impl.handler.WaitForOpponentHandler;

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
