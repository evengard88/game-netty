package me.ikurenkov.game.logic.application.impl.handler.player;

import com.google.common.base.Strings;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.ikurenkov.game.logic.application.PlayerHandler;
import me.ikurenkov.game.logic.application.port.out.MessagePort;
import me.ikurenkov.game.logic.domain.Player;

@ApplicationScoped
public class WaitForOpponentHandler implements PlayerHandler {

    @Inject
    private MessagePort messagePort;

    @Override
    public void handle(Player p, String message) {
        messagePort.sendMessage(p.getPlayerId(), "No opponent available! Wait for opponent.");
    }


    @Override
    public boolean supports(Player p) {
        return p.requiresMove() && Strings.isNullOrEmpty(p.getGameId());
    }
}
