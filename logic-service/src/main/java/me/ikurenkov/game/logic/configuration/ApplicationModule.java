package me.ikurenkov.game.logic.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import me.ikurenkov.game.logic.application.impl.GameRepoImpl;
import me.ikurenkov.game.logic.application.impl.GameServiceImpl;
import me.ikurenkov.game.logic.application.impl.RedisLobbyPort;
import me.ikurenkov.game.logic.application.port.in.LobbyPlayerStorePort;
import me.ikurenkov.game.logic.application.port.in.MessageReceivedUseCase;
import me.ikurenkov.game.logic.application.port.in.PlayerDisconnectsUseCase;
import me.ikurenkov.game.logic.application.port.in.PlayerInitializedUseCase;
import me.ikurenkov.game.logic.application.port.out.GameCreatePort;
import me.ikurenkov.game.logic.application.port.out.GameGetPort;
import me.ikurenkov.game.logic.application.port.out.GameUpdatePort;
import me.ikurenkov.game.logic.application.port.out.LobbyPlayerGetPort;

public class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PlayerInitializedUseCase.class).to(GameServiceImpl.class);
        bind(MessageReceivedUseCase.class).to(GameServiceImpl.class);
        bind(PlayerDisconnectsUseCase.class).to(GameServiceImpl.class);
        bind(GameCreatePort.class).to(GameRepoImpl.class);
        bind(GameGetPort.class).to(GameRepoImpl.class);
        bind(GameUpdatePort.class).to(GameRepoImpl.class);
        bind(LobbyPlayerStorePort.class).to(RedisLobbyPort.class);
        bind(LobbyPlayerGetPort.class).to(RedisLobbyPort.class);
    }
}