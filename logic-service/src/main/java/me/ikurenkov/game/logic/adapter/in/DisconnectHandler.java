package me.ikurenkov.game.logic.adapter.in;

import com.google.gson.Gson;
import com.google.inject.Inject;
import lombok.extern.java.Log;
import me.ikurenkov.game.logic.application.port.in.PlayerDisconnectsUseCase;
import me.ikurenkov.game.logic.configuration.LettuceModule;

import java.util.function.Consumer;

@Log
@LettuceModule.DisconnectListener
public class DisconnectHandler extends AbstractHandler<PlayerDisconnectsUseCase.Disconnection> {
    private final PlayerDisconnectsUseCase disconnectsUseCase;

    @Inject
    public DisconnectHandler(Gson mapper, PlayerDisconnectsUseCase disconnectsUseCase) {
        super(mapper);
        this.disconnectsUseCase = disconnectsUseCase;
    }

    @Override
    protected Class<PlayerDisconnectsUseCase.Disconnection> messageToClass() {
        return PlayerDisconnectsUseCase.Disconnection.class;
    }

    @Override
    protected Consumer<PlayerDisconnectsUseCase.Disconnection> execute() {
        return (d) -> disconnectsUseCase.playerDisconnect(d.getServerId(), d.getChannelId());
    }
}