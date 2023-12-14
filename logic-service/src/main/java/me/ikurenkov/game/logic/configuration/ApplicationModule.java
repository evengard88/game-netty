package me.ikurenkov.game.logic.configuration;

import com.google.inject.AbstractModule;
import me.ikurenkov.game.logic.application.impl.GameDeletePort;
import me.ikurenkov.game.logic.application.impl.GameRepoImpl;
import me.ikurenkov.game.logic.application.impl.GameServiceImpl;
import me.ikurenkov.game.logic.application.impl.RedisLobbyPort;
import me.ikurenkov.game.logic.application.port.in.LobbyPlayerStorePort;
import me.ikurenkov.game.logic.application.port.in.MessageReceivedUseCase;
import me.ikurenkov.game.logic.application.port.in.PlayerDisconnectsUseCase;
import me.ikurenkov.game.logic.application.port.out.*;

public class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MessageReceivedUseCase.class).to(GameServiceImpl.class);
        bind(PlayerDisconnectsUseCase.class).to(GameServiceImpl.class);
        bind(GameCreatePort.class).to(GameRepoImpl.class);
        bind(GameGetPort.class).to(GameRepoImpl.class);
        bind(GameUpdatePort.class).to(GameRepoImpl.class);
        bind(GameSwapPort.class).to(GameRepoImpl.class);
        bind(GameDeletePort.class).to(GameRepoImpl.class);
        bind(LobbyPlayerStorePort.class).to(RedisLobbyPort.class);
        bind(LobbyPlayerRemovePort.class).to(RedisLobbyPort.class);
    }
}