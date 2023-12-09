package com.ikurenkov.game.module;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.ikurenkov.game.domain.Player;
import com.ikurenkov.game.module.player.PlayerEnterMove;
import com.ikurenkov.game.module.player.PlayerEnterName;


public class GameConfigurationModule extends AbstractModule {
    @Override
    protected void configure() {

        MapBinder<Player.PlayerState, AbstractGameHandler> playerHandlers =
                MapBinder.newMapBinder(binder(), Player.PlayerState.class, AbstractGameHandler.class);
        playerHandlers.addBinding(Player.PlayerState.NAME_AWAIT).to(PlayerEnterName.class);
        playerHandlers.addBinding(Player.PlayerState.MOVE_AWAIT).to(PlayerEnterMove.class);
    }

}