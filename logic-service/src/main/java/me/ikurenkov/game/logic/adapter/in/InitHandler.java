package me.ikurenkov.game.logic.adapter.in;

import com.google.gson.Gson;
import com.google.inject.Inject;
import lombok.extern.java.Log;
import me.ikurenkov.game.logic.application.port.in.PlayerInitializedUseCase;
import me.ikurenkov.game.logic.configuration.LettuceModule;

import java.util.function.Consumer;

@Log
@LettuceModule.InitListener
public class InitHandler extends AbstractHandler<PlayerInitializedUseCase.Initialization> {
    private final PlayerInitializedUseCase initializedUseCase;

    @Inject
    public InitHandler(Gson mapper, PlayerInitializedUseCase initializedUseCase) {
        super(mapper);
        this.initializedUseCase = initializedUseCase;
    }

    @Override
    protected Class<PlayerInitializedUseCase.Initialization> messageToClass() {
        return PlayerInitializedUseCase.Initialization.class;
    }

    @Override
    protected Consumer<PlayerInitializedUseCase.Initialization> execute() {
        return e -> initializedUseCase.playerInitialize(e.getServerId(), e.getChannelId());
    }
}